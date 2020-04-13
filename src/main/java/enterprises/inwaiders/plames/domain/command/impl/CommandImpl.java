package enterprises.inwaiders.plames.domain.command.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import enterprises.inwaiders.plames.api.command.Command;

public abstract class CommandImpl implements Command{

	private Set<String> aliases = new HashSet<>();
	
	private boolean requireAccess = false;
	
	public void setRequireAccess(boolean i) {
		
		this.requireAccess = i;
	}
	
	public boolean isRequireAccess() {
		
		return this.requireAccess;
	}
	
	public void addAliases(String... aliases) {
		
		addAliases(Arrays.asList(aliases));
	}
	
	public void addAliases(Collection<String> aliases) {
		
		this.aliases.addAll(aliases);
	}
	
	public void setAliases(Set<String> aliases) {
		
		this.aliases = aliases;
	}
	
	public Set<String> getAliases() {
		
		return this.aliases;
	}
}
