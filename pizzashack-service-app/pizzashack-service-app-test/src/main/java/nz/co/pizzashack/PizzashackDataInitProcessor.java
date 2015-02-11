package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.io.InputStream;
import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.AwsS3GeneralService;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataInitProcessor {

	private static Set<Pizzashack> initPizzashacks = null;

	private static Injector injector = null;

	private static String LOCAL_IMAGE_FOLDER = "/images/";

	public static void main(String[] args) throws Exception {
		injector = Guice.createInjector(new RepositoryModule(), new ConfigurationServiceModule(), new SharedModule(), new ServiceModule(), new IntegrationModule());
		initPizzashacks();
	}

	private static void initPizzashacks() throws Exception {
		initPizzashacks = initPizzashackFromFile();
		final PizzashackRepository pizzashackRepository = injector.getInstance(PizzashackRepository.class);
		final AwsS3GeneralService awsS3GeneralService = injector.getInstance(AwsS3GeneralService.class);

		for (final Pizzashack pizzashack : initPizzashacks) {
			pizzashackRepository.create(pizzashack);
			final String icon = pizzashack.getIcon();
			if (!StringUtils.isEmpty(icon)) {
				final InputStream imageStream = PizzashackDataInitProcessor.class.getResourceAsStream(LOCAL_IMAGE_FOLDER + icon);
				// awsS3GeneralService.putAsset(key, asset, contentType);
			}
		}
	}
}
