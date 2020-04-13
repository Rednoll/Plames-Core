package enterprises.inwaiders.plames.domain.locale;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.AttributeConverter;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.sun.istack.logging.Logger;

import enterprises.inwaiders.plames.api.locale.PlamesLocale;
import enterprises.inwaiders.plames.dao.EntityLink;
import enterprises.inwaiders.plames.dao.locale.PlamesLocaleHlRepository;
import enterprises.inwaiders.plames.spring.SpringUtils;

//TODO: Create Id field (now is mock)
public class PlamesLocaleImpl implements PlamesLocale {

	private static transient Map<Locale, PlamesLocaleImpl> locales = new HashMap<>();
	private static transient ResourceBundleMessageSource mainSource = new ResourceBundleMessageSource();
	
	static {
		
		mainSource.setDefaultEncoding("UTF-8");
	}
	
	private Long id = null;
	
	private Locale locale = null;
	
	private transient MessageSourceAccessor messageAccessor = null;
	
	public PlamesLocaleImpl(Long id, MessageSource source, Locale locale) {
	
		this.id = id;
		this.messageAccessor = new MessageSourceAccessor(source, locale);
		this.locale = locale;
	}
	
	@Override
	public String getMessage(String tag, Object... args) {
		
		if(tag.startsWith("$")) {
			
			tag = tag.substring(1);
		}
		
		return this.messageAccessor.getMessage(tag, args);
	}
	
	public String getName() {
		
		return this.messageAccessor.getMessage("lang.name");
	}
	
	public Locale getLocale() {
		
		return this.locale;
	}
	
	public static PlamesLocaleImpl getLocale(Locale locale) {
		
		return locales.get(locale);
	}

	public static List<PlamesLocaleImpl> getOrderedByName() {
		
		List<PlamesLocaleImpl> all = getAll();
		
		all.sort((PlamesLocaleImpl a, PlamesLocaleImpl b) -> a.getName().compareToIgnoreCase(b.getName()));
		
		return all;
	}
	
	public Long getId() {
		
		return this.id;
	}
	
	public static List<PlamesLocaleImpl> getAll() {
	
		return new ArrayList<>(locales.values());
	}
	
	public static class HighLevelRepository extends PlamesLocaleHlRepository<PlamesLocaleImpl> {
		
		@Override
		public EntityLink getLink(PlamesLocaleImpl locale) {
			
			return new EntityLink(SpringUtils.getEntityName(PlamesLocaleImpl.class), locale.getId());
		}
		
		@Override
		public PlamesLocale getLocale(Locale locale) {
			
			return PlamesLocaleImpl.getLocale(locale);
		}
		
		public void save(PlamesLocaleImpl locale) {
			
			//TODO
		}
		
		public PlamesLocaleImpl getById(Long id) {
			
			return null;
		}
		
		@Override
		public List<PlamesLocale> getAll() {
			
			return new ArrayList<>(PlamesLocaleImpl.getAll());
		}
		
		public static class PlamesLocaleConverter implements AttributeConverter<PlamesLocale, String> {

			@Override
			public String convertToDatabaseColumn(PlamesLocale attribute) {
				
				if(attribute == null || attribute.getLocale() == null) return null;
				
				Locale locale = attribute.getLocale();
				
				return locale.getLanguage()+"_"+locale.getCountry();
			}

			@Override
			public PlamesLocale convertToEntityAttribute(String dbData) {
				
				if(dbData == null || dbData.isEmpty()) return null;
				
				String[] rawLocale = dbData.split("_");
				
				Locale locale = null;
				
				if(rawLocale.length == 1) {
					
					locale = new Locale(rawLocale[0]);
				}
				
				if(rawLocale.length == 2) {
					
					locale = new Locale(rawLocale[0], rawLocale[1]);
				}
				
				return PlamesLocale.getLocale(locale);
			}
		}
	}
	
	public static void loadBundles(File bundlesDir) {
		
		if(!bundlesDir.isDirectory()) {
			
			throw new RuntimeException("Bundle file must be a dir!");
		}
		
		for(File file : bundlesDir.listFiles()) {
			
			if(file.isDirectory()) {
				
				loadBundles(file);
			}
			else {
				
				String bundleName = file.getName();
				
				if(bundleName.contains(".")) {
					
					bundleName = bundleName.substring(0, bundleName.lastIndexOf("."));
				}

				if(bundleName.contains("_")) {
					
					String[] bundleNameData = bundleName.split("_");
					
					String baseName = "file:"+bundlesDir.getPath()+"/"+bundleNameData[0];
					
					mainSource.addBasenames(baseName);
				
					Logger.getLogger(PlamesLocaleImpl.class).info("Found localization bundle: "+baseName);
				
					Locale locale = null;
					
					if(bundleNameData.length == 2) {
						
						locale = new Locale(bundleNameData[1]);
					}
					
					if(bundleNameData.length == 3) {
						
						locale = new Locale(bundleNameData[1], bundleNameData[2]);
					}
					
					if(locale != null && !locales.containsKey(locale)) {
						
						PlamesLocaleImpl newPlamesLocale = new PlamesLocaleImpl(null, mainSource, locale);
					
						Logger.getLogger(PlamesLocaleImpl.class).info("Created new plames locale: "+locale.toString());
						
						locales.put(locale, newPlamesLocale);
					}
				}
			}
		}
	}
	
	public static void initSystemLocale() {
	
		PlamesLocaleHlRepository rep = PlamesLocaleHlRepository.getRepository();
	
		Locale locale = Locale.getDefault();
		
		if(locale != null) {
			
			PlamesLocale plamesLocale = rep.getLocale(locale);
		
			if(plamesLocale != null) {
				
				rep.setSystemLocale(plamesLocale);
			}
		}
	}
}
