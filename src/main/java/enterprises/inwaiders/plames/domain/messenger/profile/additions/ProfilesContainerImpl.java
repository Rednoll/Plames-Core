package enterprises.inwaiders.plames.domain.messenger.profile.additions;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import enterprises.inwaiders.plames.api.messenger.profile.UserProfile;
import enterprises.inwaiders.plames.api.messenger.profile.additions.ProfilesContainer;
import enterprises.inwaiders.plames.domain.messenger.profile.impl.UserProfileBase;

@Embeddable
public class ProfilesContainerImpl implements ProfilesContainer{

	@OneToMany(targetEntity = UserProfileBase.class, cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private Set<UserProfile> profiles = new HashSet<>();
	
	@JoinColumn(name = "primary_profile_id")
	@OneToOne(targetEntity = UserProfileBase.class)
	private UserProfile primaryProfile = null;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((primaryProfile == null) ? 0 : primaryProfile.hashCode());
		result = prime * result + ((profiles == null) ? 0 : profiles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfilesContainerImpl other = (ProfilesContainerImpl) obj;
		if (primaryProfile == null) {
			if (other.primaryProfile != null)
				return false;
		} else if (!primaryProfile.equals(other.primaryProfile))
			return false;
		if (profiles == null) {
			if (other.profiles != null)
				return false;
		} else if (!profiles.equals(other.profiles))
			return false;
		return true;
	}

	public UserProfile pickProfile() {
		
		if(primaryProfile != null && primaryProfile.isOnline()) return this.primaryProfile;
	
		for(UserProfile profile : profiles) {
			
			if(profile.isOnline()) {
				
				return profile;
			}
		}
		
		return null;
	}
	
	public void setPrimaryProfile(UserProfile profile) {
		
		this.primaryProfile = profile;
	}
	
	public UserProfile getPrimaryProfile() {
		
		return this.primaryProfile;
	}
	
	public void addProfile(UserProfile profile) {
		
		this.profiles.add(profile);
	}
	
	public boolean hasProfile(String messengerType) {
		
		return !getProfiles(messengerType).isEmpty();
	}
	
	public UserProfile getOneProfile(String messengerType) {
		
		Set<UserProfile> profiles = getProfiles(messengerType);
		
		if(!profiles.isEmpty()) {
			
			return profiles.iterator().next();
		}
		
		return null;
	}
	
	public Set<UserProfile> getProfiles(String messengerType) {
		
		Set<UserProfile> result = new TreeSet<UserProfile>((UserProfile o1, UserProfile o2)-> o1.getId().compareTo(o2.getId()));
		
		for(UserProfile profile : profiles) {
			
			if(profile.getMessengerType().equals(messengerType)) {
				
				result.add(profile);
			}
		}
		
		return result;
	}
	
	public void removeProfile(UserProfile profile) {
		
		this.profiles.remove(profile);
	}
	
	public Set<UserProfile> getProfiles() {
		
		return this.profiles;
	}
}
