package enterprises.inwaiders.plames.domain.procedure.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import enterprises.inwaiders.plames.api.procedure.Procedure;
import enterprises.inwaiders.plames.api.procedure.ProcedureStage;
import enterprises.inwaiders.plames.api.procedure.ProcedureStageRunResult;
import enterprises.inwaiders.plames.api.procedure.ProcedureStageStatus;
import enterprises.inwaiders.plames.dao.procedure.ProcedureRepository;

@Entity
@Table(name = "procedures")
@Inheritance(strategy = InheritanceType.JOINED)
public class ProcedureImpl<S extends ProcedureStage> implements Procedure<S>{
	
	protected static transient ProcedureRepository<ProcedureImpl> repository;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected Long id;
	
	@Transient
	private List<S> stages = new ArrayList<S>();
	
	@Column(name = "iterator")
	private int iterator = 0;
	
	@Column(name = "is_began")
	private boolean isBegan = false;
	
	@Column(name = "is_end")
	protected boolean isEnd = false;
	
	@Column(name = "auto_delete")
	protected boolean autoDelete = true;
	
	@Column(name = "create_time")
	protected long createTime = System.currentTimeMillis();
	
	@Column(name = "lifetime")
	protected long lifetime = -1;
	
	@Column(name = "deleted")
	protected volatile boolean deleted = false;
	
	@PostLoad
	private void postLoad() {
		
		if(lifetime < 0) return;
		if(deleted) return;
		
		if(lifetime+createTime < System.currentTimeMillis()) {
			
			this.isEnd = true;
			onLifetimeEnd();
			delete();
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, isBegan, isEnd, iterator, stages, deleted, autoDelete, lifetime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcedureImpl other = (ProcedureImpl) obj;
		return Objects.equals(id, other.id) && isBegan == other.isBegan && isEnd == other.isEnd
				&& iterator == other.iterator && Objects.equals(stages, other.stages) && deleted == other.deleted && autoDelete == other.autoDelete
				&& lifetime == other.lifetime;
	}

	public void begin() {

		onBegin();

		this.isBegan = true;
	}
	
	public void abort() {
		
		onFail();
		
		this.isEnd = true;
	}
	
	@Override
	public void onBegin() {
		
	}

	@Override
	public void onComplete() {
		
	}

	@Override
	public void onFail() {
		
	}
	
	@Override
	public void onLifetimeEnd() {
	
		System.out.println("Lifetime end!");
	}

	@Override
	public boolean runNextStage(String... args) {

		if(!isBegan()) {
			
			begin();
		}
		
		if(!hasNextStage() || isEnd()) return false;
			
		S stage = stages.get(this.iterator);
		
		ProcedureStageRunResult result = stage.run(this, args);
	
		if(result.getStatus() == ProcedureStageStatus.OK) {
			
			this.iterator++;
		}
		else if(result.getStatus() == ProcedureStageStatus.REPEAT) {
			///...
		}
		else if(result.getStatus() == ProcedureStageStatus.ERROR) {
			
			onFail();
			
			this.isEnd = true;
			
			throw new RuntimeException("Procedure error: "+result.getMessage());
		}
		else if(result.getStatus() == ProcedureStageStatus.ABORT) {
			
			this.isEnd = true;
		}
		else if(result.getStatus() == ProcedureStageStatus.COMPLETE) {
			
			onComplete();
			
			this.isEnd = true;
		}
		else if(result.getStatus() == ProcedureStageStatus.JUMP) {
			
			runNextStage(args);
		}
		
		if(this.iterator == stages.size() && !this.isEnd) {
			
			onComplete();
		
			this.isEnd = true;
		}
		
		if(this.isEnd && this.autoDelete) {
			
			delete();
		}

		save();
		
		return true;
	}
	
	public void setLifetime(long lifetime) {
	
		this.lifetime = lifetime;
	}
	
	public long getLifetime() {
		
		return this.lifetime;
	}
	
	public long getCreateTime() {
		
		return this.createTime;
	}
	
	@Override
	public void setAutoDelete(boolean autoDelete) {
		
		this.autoDelete = autoDelete;
	}
	
	@Override
	public boolean getAutoDelete() {
		
		return this.autoDelete;
	}
	
	@Override
	public void skipStages(int stagesCount) {
		
		this.iterator += stagesCount;
	}

	@Override
	public void goToStage(int stageIndex) {
		
		this.iterator = stageIndex;
	}
	
	@Override
	public boolean hasNextStage() {
		
		return this.iterator < stages.size();
	}

	@Override
	public int getStagesCount() {
		
		return stages.size();
	}
	
	@Override
	public boolean isBegan() {
		
		return this.isBegan;
	}
	
	public boolean isEnd() {
	
		return this.isEnd;
	}
	
	public List<S> getStages() {
	
		return this.stages;
	}
	
	@Override
	public Long getId() {
		
		return this.id;
	}
	
	@Override
	public void save() {
		
		if(!deleted) {
			
			repository.save(this);
		}
	}
	
	public void delete() {
		
		deleted = true;
		repository.save(this);
	}
	
	public static void flush() {
		
		repository.flush();
	}
	
	public static ProcedureImpl getById(long id) {
		
		return repository.getOne(id);
	}
	
	public static List<ProcedureImpl> getAll() {
		
		return repository.findAll();
	}
	
	public static void setRepository(ProcedureRepository rep) {
		
		repository = rep;
	}
}
