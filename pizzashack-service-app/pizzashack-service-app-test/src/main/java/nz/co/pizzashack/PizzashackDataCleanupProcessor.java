package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataCleanupProcessor {

	private static Set<Pizzashack> initPizzashacks = null;

	public static void main(String[] args) throws Exception {
		initPizzashacks = initPizzashackFromFile();
		Injector injector = Guice.createInjector(new RepositoryModule(), new ConfigurationServiceModule(), new SharedModule());
		final PizzashackRepository pizzashackRepository = injector.getInstance(PizzashackRepository.class);

		for (final Pizzashack pizzashack : initPizzashacks) {
			pizzashackRepository.deleteById(pizzashack.getPizzashackId());
		}
	}
}
