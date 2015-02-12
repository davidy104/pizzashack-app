package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.io.InputStream;
import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.camel.CamelContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataInitProcessor {

	private static Injector injector;

	private static String LOCAL_IMAGE_FOLDER = "/images/";

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackDataInitProcessor.class);

	public static void main(String[] args) throws Exception {
		injector = Guice.createInjector(new ConfigurationServiceModule(), new SharedModule(), new RepositoryModule(),
				new IntegrationModule(), new ServiceModule());
		CamelContext camelContext = injector.getInstance(CamelContext.class);
		camelContext.start();
		initPizzashacks();
		camelContext.stop();
	}

	public static void initPizzashacks() throws Exception {
		Set<Pizzashack> initPizzashacks = initPizzashackFromFile();
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		for (final Pizzashack pizzashack : initPizzashacks) {
			InputStream imageStream = null;
			String imageName = pizzashack.getIcon();
			LOGGER.info("imageName:{} ", imageName);
			if (!StringUtils.isEmpty(imageName)) {
				imageStream = PizzashackDataInitProcessor.class.getResourceAsStream(LOCAL_IMAGE_FOLDER + imageName);
				LOGGER.info("imageStream size:{} ", imageStream.available());
			}
			pizzashackDS.createPizzashack(pizzashack, pizzashack.getIcon(), imageStream);
		}
	}
}
