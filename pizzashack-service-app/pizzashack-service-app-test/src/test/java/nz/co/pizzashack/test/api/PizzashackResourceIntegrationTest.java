package nz.co.pizzashack.test.api;

import static nz.co.pizzashack.util.JerseyClientUtil.getResponsePayload;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.service.ServiceModule;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.util.RestClientExecuteCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, ServiceModule.class, RepositoryModule.class })
public class PizzashackResourceIntegrationTest {

	private final static String API_BASE_URI = "http://localhost:8181/pizzashackApp/pizzashack";

	@Inject
	private Client jerseyClient;

	private GeneralJsonRestClientAccessor generalJsonRestClientAccessor;

	private final static String TEST_IMAGE = "1.png";

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackResourceIntegrationTest.class);

	@Before
	public void setup() throws Exception {
		generalJsonRestClientAccessor = new GeneralJsonRestClientAccessor(jerseyClient, API_BASE_URI);
	}

	@Test
	public void testCreate() throws Exception {
		final FormDataMultiPart multiPart = getCreateForm();
		final String responseStr = generalJsonRestClientAccessor.doProcess(API_BASE_URI, "/upload", ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, multiPart);
			}
		});
		LOGGER.info("responseStr:{} ", responseStr);
	}

	private FormDataMultiPart getCreateForm() throws Exception {
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.field("name", TEST_IMAGE);
		multiPart.field("tag", "testtag");
		byte[] imageBytes = Resources.toByteArray(Resources.getResource(TEST_IMAGE));
		final FormDataContentDisposition dispo = FormDataContentDisposition
				.name("image")
				.size(imageBytes.length)
				.build();
		final FormDataBodyPart imageBodyPart = new FormDataBodyPart(dispo, imageBytes, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(imageBodyPart);
		return multiPart;
	}

	@Test
	public void testGetAll() throws Exception {
		final String responseJson = generalJsonRestClientAccessor.get("/list");
		LOGGER.info("responseJson:{} ", responseJson);
	}

	@Test
	public void testBasicCRUD() throws Exception {
		Pizzashack add = new Pizzashack.Builder().pizzaName("testPizzaname").description("testDesc").build();
		String json = jacksonObjectMapper.writeValueAsString(add);
		final ClientResponse clientResponse = generalJsonRestClientAccessor.simpleCreate(json);
		URI addedUri = clientResponse.getLocation();
		assertNotNull(addedUri);
		LOGGER.info("addedUri:{} ", addedUri.toString());
		final String addedId = getResponsePayload(clientResponse);

		String getResponse = generalJsonRestClientAccessor.get("/" + addedId);
		LOGGER.info("getResponse:{} ", getResponse);

		generalJsonRestClientAccessor.delete("/" + addedId);

		try {
			generalJsonRestClientAccessor.get("/" + addedId);
		} catch (final NotFoundException e) {
			LOGGER.info("pizzashack deleted");
			e.printStackTrace();
		}
	}
}
