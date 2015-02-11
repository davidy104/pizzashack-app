package nz.co.pizzashack.service;

import nz.co.pizzashack.service.impl.PizzashackDSImpl;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PizzashackDS.class).to(PizzashackDSImpl.class).asEagerSingleton();
	}

}
