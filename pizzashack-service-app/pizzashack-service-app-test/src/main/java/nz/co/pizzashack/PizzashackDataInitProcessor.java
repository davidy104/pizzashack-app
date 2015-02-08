package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataInitProcessor {

	private final static String PIZZASHACK_INIT_FILE = "pizzashack-init.txt";
	
	private static Set<Pizzashack> initPizzashacks = null;
	
	private static Injector injector = null;
	
	public static void main(String[] args) throws Exception {
		injector = Guice.createInjector(new RepositoryModule(),new ConfigurationServiceModule(),new SharedModule());
		initPizzashacks();
	}
	
	private static void initPizzashacks()throws Exception {
		initPizzashacks = initPizzashackFromFile(PIZZASHACK_INIT_FILE);
		final PizzashackRepository pizzashackRepository = injector.getInstance(PizzashackRepository.class);
		for(final Pizzashack pizzashack : initPizzashacks){
			pizzashackRepository.create(pizzashack);
		}
	}
}
