package nz.co.pizzashack.util;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import nz.co.pizzashack.AbstractEnumQueryParameter;
import nz.co.pizzashack.PagingAndSortingParameter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GeneralJsonRestClientAccessor extends GeneralRestClientAccessor {

	public GeneralJsonRestClientAccessor(final Client jerseyClient, final String hostUri) {
		super(jerseyClient, hostUri);
	}

	public String query(final String path, final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters) throws Exception {
		return process(path, emunQueryParameters, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
	}

	public String get(final String path) throws Exception {
		return process(path, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
	}

	public void delete(final String path) throws Exception {
		process(path, ClientResponse.Status.NO_CONTENT.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.delete(ClientResponse.class);
			}
		});
	}

	public String paginate(final String path, Map<AbstractEnumQueryParameter, String> emunQueryParameters, final Integer pageOffset, final Integer pageSize) throws Exception {
		if (pageOffset != null) {
			emunQueryParameters.put(PagingAndSortingParameter.start, String.valueOf(pageOffset));
		}
		if (pageSize != null) {
			emunQueryParameters.put(PagingAndSortingParameter.size, String.valueOf(pageSize));
		}
		return process(path, emunQueryParameters, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
	}

	public String create(final String path, final String jsonBody) throws Exception {
		return process(path, ClientResponse.Status.CREATED.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonBody);
			}
		});
	}

	public String update(final String path, final String updateJsonBody) throws Exception {
		return process(path, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, updateJsonBody);
			}
		});
	}
}
