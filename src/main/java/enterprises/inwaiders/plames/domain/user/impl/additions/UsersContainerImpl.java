package enterprises.inwaiders.plames.domain.user.impl.additions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

import enterprises.inwaiders.plames.api.user.User;
import enterprises.inwaiders.plames.api.user.additions.UsersContainer;
import enterprises.inwaiders.plames.domain.user.impl.UserImpl;

@Embeddable
public class UsersContainerImpl implements UsersContainer {
	
	@ManyToMany(targetEntity = UserImpl.class)
	private Set<User> users = new HashSet<>();
	
	public User getByNickname(String name) {
		
		for(User user : users) {
			
			if(user.getNickname().equals(name)) {
				
				return user;
			}
		}
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		UsersContainerImpl other = (UsersContainerImpl) obj;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

	public void setUsers(Set<User> users) {
		
		this.users = users;
	}
	
	public Set<User> getUsers() {
	
		return this.users;
	}

	@Override
	public boolean add(User u) {
		
		return users.add(u);
	}

	@Override
	public boolean addAll(Collection<? extends User> c) {
		
		return users.addAll(c);
	}

	@Override
	public void clear() {
		
		users.clear();
	}

	@Override
	public boolean contains(Object o) {
		
		return users.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		
		return users.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
	
		return users.isEmpty();
	}

	@Override
	public Iterator<User> iterator() {
		
		return users.iterator();
	}

	@Override
	public boolean remove(Object o) {
	
		return users.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		
		return users.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		
		return users.retainAll(c);
	}

	@Override
	public int size() {
		
		return users.size();
	}

	@Override
	public Object[] toArray() {
		
		return users.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
	
		return users.toArray(a);
	}
}
