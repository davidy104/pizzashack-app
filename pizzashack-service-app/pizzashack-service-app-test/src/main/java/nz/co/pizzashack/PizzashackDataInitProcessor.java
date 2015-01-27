package nz.co.pizzashack;

import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataInitProcessor {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new RepositoryModule());
		final PizzashackRepository pizzashackRepository = injector.getInstance(PizzashackRepository.class);
	}
}
