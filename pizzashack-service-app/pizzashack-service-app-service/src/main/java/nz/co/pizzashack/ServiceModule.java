package nz.co.pizzashack;

import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.ds.impl.PizzashackDSImpl;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PizzashackDS.class).to(PizzashackDSImpl.class).asEagerSingleton();
	}

}
