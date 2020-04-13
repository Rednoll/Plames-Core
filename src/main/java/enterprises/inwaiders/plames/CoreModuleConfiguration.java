package enterprises.inwaiders.plames;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:config/modules/core/main.properties")
@ConfigurationProperties(prefix="core", ignoreInvalidFields = true, ignoreUnknownFields = true)
public class CoreModuleConfiguration {

	@Value("${localization.bundles:localization/}")
	private String bundlesRootDir;
	
	public String getBundlesRootDir() {
		
		return this.bundlesRootDir;
	}
}