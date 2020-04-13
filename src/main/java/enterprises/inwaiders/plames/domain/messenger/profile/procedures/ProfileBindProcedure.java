package enterprises.inwaiders.plames.domain.messenger.profile.procedures;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import enterprises.inwaiders.plames.CoreModule;
import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.procedure.ProcedureStage;
import enterprises.inwaiders.plames.api.procedure.ProcedureStageRunResult;
import enterprises.inwaiders.plames.api.procedure.ProcedureStageStatus;
import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.dao.messenger.profile.procedures.ProfileBindProcedureRepository;
import enterprises.inwaiders.plames.domain.locale.PlamesLocaleImpl;
import enterprises.inwaiders.plames.domain.locale.PlamesLocaleImpl.HighLevelRepository.PlamesLocaleConverter;
import enterprises.inwaiders.plames.domain.user.impl.UserImpl;
import enterprises.inwaiders.plames.system.utils.MessageUtils;
import enterprises.inwaiders.plames.system.utils.code.VerifyCodeGenerator;

@Entity
@Table(name = "profile_bind_procedures")
public class ProfileBindProcedure extends ProfileProcedure<ProcedureStage<ProfileBindProcedure>>{

	private static transient ProfileBindProcedureRepository repository = null;
	
	public static VerifyCodeGenerator codeGenerator = new VerifyCodeGenerator(new File("./data/modules/core/ProfileBindProcedure.codegen"));
	
	@Column(name = "suspect_nickname")
	private String suspectNickname = "";
	
	@Column(name = "code")
	private String code = "";

	@Column(name = "locale")
	@Convert(converter = PlamesLocaleConverter.class)
	private PlamesLocaleImpl choosedLocale = null;
	
	@Transient
	private List<PlamesLocaleImpl> locales = null;
	
	private ProfileBindProcedure(UserProfile profile) {
		this();
		
		this.profile = profile;
	}
	
	public ProfileBindProcedure() {
		
		this.setAutoDelete(false);
		this.setLifetime(15*60*1000);
		codeGenerator.setCodeLifetime(this.getLifetime());
		
		this.locales = PlamesLocaleImpl.getOrderedByName();
		
		this.getStages().add(new ProcedureStage<ProfileBindProcedure>() {
			
			@Override
			public ProcedureStageRunResult run(ProfileBindProcedure procedure, String... args) {
				
				String rawIndex = args[0];
				
				int index = Integer.valueOf(rawIndex)-1;
				
				choosedLocale = locales.get(index);
				
				MessageUtils.send(CoreModule.getSystemProfile(), profile, choosedLocale.getMessage("locale.choosed"));
				MessageUtils.send(CoreModule.getSystemProfile(), profile, choosedLocale.getMessage("procedure.profile_bind.name_request", profile.getMessengerType()));
				
				return new ProcedureStageRunResult(ProcedureStageStatus.OK);
			}
		});
		
		this.getStages().add(new ProcedureStage<ProfileBindProcedure>() {
			
			@Override
			public ProcedureStageRunResult run(ProfileBindProcedure procedure, String... args) {
				
				String text = args[0];
				
				if(text.equalsIgnoreCase(choosedLocale.getMessage("lang.accept"))) {
					
					User user = UserImpl.create();
						user.setNickname(suspectNickname);
						user.setLocale(choosedLocale);
						
					profile.setUser(user);

					MessageUtils.send(CoreModule.getSystemProfile(), profile, choosedLocale.getMessage("procedure.profile_bind.reg_complete", user.getNickname(), profile.getMessengerType()));
					
					return new ProcedureStageRunResult(ProcedureStageStatus.COMPLETE);
				}
				else {
				
					suspectNickname = text;
					
					User user = UserImpl.getByNickname(suspectNickname);
					
					if(user != null) {
						
						code = codeGenerator.gen();
						
						if(user.getLocale() == null) {
							
							user.setLocale(choosedLocale);
						}
						
						MessageUtils.send(CoreModule.getSystemProfile(), profile, choosedLocale.getMessage("procedure.profile_bind.connect_complete", profile.getHumanSign(), user.getNickname(), "/bind "+code));
						
						return new ProcedureStageRunResult(ProcedureStageStatus.COMPLETE);
					}
					else {
						
						MessageUtils.send(CoreModule.getSystemProfile(), profile, choosedLocale.getMessage("procedure.profile_bind.try_reg", suspectNickname));
						
						return new ProcedureStageRunResult(ProcedureStageStatus.REPEAT);
					}
				}
			}
		});
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.code, this.suspectNickname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ProfileBindProcedure))
			return false;
		ProfileBindProcedure other = (ProfileBindProcedure) obj;
		return Objects.equals(code, other.code) && Objects.equals(suspectNickname, other.suspectNickname);
	}
	
	@Override
	public void onBegin() {
		super.onBegin();
		
		StringBuilder builder = new StringBuilder();
		
			for(int i = 0; i < locales.size(); i++) {
			
				builder.append((i+1)+". "+locales.get(i).getName());
			
				if(i+1 != locales.size()) {
					
					builder.append("\n");
				}
			}
			
		MessageUtils.send(CoreModule.getSystemProfile(), profile, builder.toString());
	}
	
	public static ProfileBindProcedure create(UserProfile profile) {
		
		ProfileBindProcedure procedure = new ProfileBindProcedure(profile);

		procedure = repository.saveAndFlush(procedure);
		
		return procedure;
	}
	
	public static ProfileBindProcedure getByCode(String code) {
		
		return repository.getByCode(code);
	}
	
	public static void setRepository(ProfileBindProcedureRepository rep) {
		
		repository = rep;
	}
}
