package com.inwaiders.plames.system.utils.code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.inwaiders.plames.system.utils.code.strategy.CodeGenStrategy;
import com.inwaiders.plames.system.utils.code.strategy.impl.DefaultCodeGenStrategy;

public class VerifyCodeGenerator {

	private File dataFile = null;
	
	private CodeGenStrategy strategy = new DefaultCodeGenStrategy();
	
	private Map<String, Long> codes = new HashMap<>();
	
	private long codeLifetime = 1;
	
	private int clearRate = 100;
	private int clearIterator = 0;
	
	public VerifyCodeGenerator(File dataFile) {
		
		this.dataFile = dataFile;
		
		loadFromFile(dataFile);
	}
	
	public String gen() {
		
		if(codes.size() == strategy.getCombinationsCount()) throw new RuntimeException("Codes are over! Count: "+codes.size());
		
		while(true) {
			
			String code = strategy.gen();
		
			if(!codes.containsKey(code)) {
				
				codes.put(code, System.currentTimeMillis());
				
				if(dataFile != null) {
					
					saveToFile(dataFile);
				}
				
				clearIterator++;
				
				if(clearIterator >= clearRate) {
					
					clearByLifetime();
					clearIterator = 0;
				}
				
				return code;
			}
		}
	}
	
	public boolean consume(String code) {

		Long codeCreateTime = codes.get(code);
		
		if(codeCreateTime == null) return false;
		
		if(codeCreateTime+codeLifetime >= System.currentTimeMillis()) {
			
			codes.remove(code);
			
			return true;
		}
		
		return false;
	}
	
	public void saveToFile(File file) {
		
		clearByLifetime();
		
		StringBuilder builder = new StringBuilder();
			builder.append("CodeLifetime: "+codeLifetime+"\n");
			builder.append("ClearRate: "+clearRate);
			
			for(Entry<String, Long> entry : codes.entrySet()) {
				
				builder.append("\n"+entry.getKey()+" "+entry.getValue());
			}
		
		try {

			if(!Files.exists(file.getParentFile().toPath())) {
				
				Files.createDirectories(file.getParentFile().toPath());
			}
			
			Files.write(file.toPath(), builder.toString().getBytes());
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void loadFromFile(File file) {
		
		try {
			
			if(!file.exists()) return;
			
			List<String> lines = Files.readAllLines(file.toPath());
		
			this.codeLifetime = Long.valueOf(lines.get(0).substring("CodeLifetime: ".length()));
			this.clearRate = Integer.valueOf(lines.get(1).substring("ClearRate: ".length()));
			
			for(int i = 0;i<2;i++) {
				
				lines.remove(0);
			}
			
			for(String line : lines) {
				
				String[] raw = line.split(" ");
				String code = raw[0];
				Long createTime = Long.valueOf(raw[1]);
			
				codes.put(code, createTime);
			}
			
			clearByLifetime();
		}
		catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private void clearByLifetime() {
		
		codes.entrySet().removeIf((Entry<String, Long> entry) -> entry.getValue()+codeLifetime < System.currentTimeMillis());
	}
	
	public void setCodeLifetime(long lifeTime) {
		
		this.codeLifetime = lifeTime;
	}
	
	public long getCodeLifetime() {

		return this.codeLifetime;
	}
	
	public void setDataFile(File file) {
		
		this.dataFile = file;
	}
	
	public File getDataFile() {
		
		return this.dataFile;
	}
	
	public void setCodeGenStrategy(CodeGenStrategy strategy) {
		
		this.strategy = strategy;
	}
	
	public CodeGenStrategy getCodeGenStrategy() {
		
		return this.strategy;
	}
}
