package nz.co.pizzashack.config;

import nz.co.pizzashack.resources.PizzashackResource;

import com.google.inject.AbstractModule;

public class ResourceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PizzashackResource.class);
	}
}
