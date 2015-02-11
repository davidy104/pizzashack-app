package nz.co.pizzashack.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import nz.co.pizzashack.ServiceModule;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.AwsS3GeneralService;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.S3Asset;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, ServiceModule.class, RepositoryModule.class, IntegrationModule.class })
public class ImageToS3RouteIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageToS3RouteIntegrationTest.class);

	@Produce(uri = "direct:ImageToS3")
	private ProducerTemplate producerTemplate;

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	private final static String TEST_IMAGE_NAME = "10.png";

	private final static String IMAGE_PATH = "/image/pizza/";

	@Inject
	private CamelContext camelContext;

	private InputStream imageStream;

	@Inject
	private AwsS3GeneralService awsS3GeneralService;

	@Before
	public void setUp() throws Exception {
		imageStream = ImageToS3RouteIntegrationTest.class.getResourceAsStream("/" + TEST_IMAGE_NAME);
		camelContext.start();
	}

	@After
	public void tearDown() throws Exception {
		camelContext.stop();
	}

	@Test
	public void test() {
		producerTemplate.sendBodyAndProperty(imageStream, "outputPath", IMAGE_PATH + TEST_IMAGE_NAME);
		S3Asset uploadedAssert = awsS3GeneralService.getAssetByName(IMAGE_PATH + TEST_IMAGE_NAME);
		assertNotNull(uploadedAssert);
		assertEquals(uploadedAssert.getKey(), IMAGE_PATH + TEST_IMAGE_NAME);
		LOGGER.info("uploadedAssert:{} ", uploadedAssert);
		awsS3GeneralService.deleteAssert(IMAGE_PATH + TEST_IMAGE_NAME);
	}

}
