package nz.co.pizzashack.test.ds;

import static nz.co.pizzashack.test.TestUtils.initPizzashackFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import nz.co.pizzashack.ServiceModule;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.apache.camel.CamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.model.S3Object;
import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, ServiceModule.class, RepositoryModule.class, IntegrationModule.class })
public class PizzashackDSIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackDSIntegrationTest.class);
	@Inject
	private CamelContext camelContext;

	@Inject
	private PizzashackDS pizzashackDS;

	private static Set<Pizzashack> initialPizzashacks = Collections.<Pizzashack> emptySet();
	private Pizzashack initPizzashack = null;

	@BeforeClass
	public static void setUp() throws Exception {
		initialPizzashacks = initPizzashackFromFile();
		assertNotNull(initialPizzashacks);
		assertEquals(initialPizzashacks.size(), 2);
	}

	@Before
	public void init() throws Exception {
		camelContext.start();
		initPizzashack = (Pizzashack) initialPizzashacks.toArray()[0];
		final String imageName = initPizzashack.getIcon();
		final InputStream imageStream = PizzashackDSIntegrationTest.class.getResourceAsStream("/" + imageName);
		final String id = pizzashackDS.createPizzashack(initPizzashack, imageName, imageStream);
		initPizzashack.setPizzashackId(id);
		LOGGER.info("initPizzashack:{} ", initPizzashack);
		assertNotNull(initPizzashack);

	}

	@After
	public void tearDown() throws Exception {
		if (initPizzashack != null) {
			pizzashackDS.deleteById(initPizzashack.getPizzashackId());
		}
		camelContext.stop();
	}

	@Test
	public void testGetImage() throws Exception {
		S3Object found = pizzashackDS.loadImageFromS3("5.png");
		assertNotNull(found);
		LOGGER.info("found:{} ", found);
	}

	@Test
	public void testCRUD() {
		// pizzashackDS.createPizzashack(addPizzashack, imageName, imageStream);
	}

}
