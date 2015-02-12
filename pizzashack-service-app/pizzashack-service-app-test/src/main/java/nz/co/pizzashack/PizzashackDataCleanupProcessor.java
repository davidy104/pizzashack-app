package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.camel.CamelContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataCleanupProcessor {

	private static Injector injector = null;

	public static void main(String[] args) throws Exception {
		injector = Guice.createInjector(new ConfigurationServiceModule(), new SharedModule(), new RepositoryModule(),
				new IntegrationModule(), new ServiceModule());
		CamelContext camelContext = injector.getInstance(CamelContext.class);
		camelContext.start();
		try {
			cleanPizzashacks();
		} catch (Exception e) {
			throw e;
		} finally {
			camelContext.stop();
		}
	}

	public static void cleanPizzashacks() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		Set<Pizzashack> initPizzashacks = initPizzashackFromFile();
		for (final Pizzashack pizzashack : initPizzashacks) {
			pizzashackDS.deleteById(pizzashack.getPizzashackId());
		}
	}
}
