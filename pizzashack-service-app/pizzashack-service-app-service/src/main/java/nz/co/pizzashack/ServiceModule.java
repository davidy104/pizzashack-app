package nz.co.pizzashack;

import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.ds.impl.PizzashackDSImpl;
import nz.co.pizzashack.ds.impl.UserDSImpl;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PizzashackDS.class).to(PizzashackDSImpl.class).asEagerSingleton();
		bind(UserDS.class).to(UserDSImpl.class).asEagerSingleton();
	}

}
