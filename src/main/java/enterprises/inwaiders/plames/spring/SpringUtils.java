package enterprises.inwaiders.plames.spring;

import javax.persistence.Entity;

import org.hibernate.Hibernate;

public class SpringUtils {

	public static String getEntityName(Object entity) {
		
		return getEntityName(Hibernate.unproxy(entity).getClass());
	}
	
	public static String getEntityName(Class<?> clazz) {
		
		String name = clazz.getAnnotation(Entity.class).name();
		
		if(name == null || name.isEmpty()) {
			
			return clazz.getSimpleName();
		}
		
		return name;
	}
}
