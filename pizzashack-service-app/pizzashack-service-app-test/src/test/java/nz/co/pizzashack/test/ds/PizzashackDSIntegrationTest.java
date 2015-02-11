package nz.co.pizzashack.test.ds;

import nz.co.pizzashack.ServiceModule;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.apache.camel.CamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, ServiceModule.class, RepositoryModule.class, IntegrationModule.class })
public class PizzashackDSIntegrationTest {

	@Inject
	private CamelContext camelContext;

	@Inject
	private PizzashackDS pizzashackDS;

	@Before
	public void setUp() throws Exception {
		camelContext.start();
	}

	@After
	public void tearDown() throws Exception {
		camelContext.stop();
	}

	@Test
	public void testCRUD() {
//		pizzashackDS.createPizzashack(addPizzashack, imageName, imageStream);
	}

}
