package nz.co.pizzashack.util;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import nz.co.pizzashack.AbstractEnumQueryParameter;
import nz.co.pizzashack.PagingAndSortingParameter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GeneralJsonRestClientAccessor extends GeneralRestClientAccessor {

	public GeneralJsonRestClientAccessor(final Client jerseyClient, final String hostUri) {
		super(jerseyClient, hostUri);
	}

	public String query(final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters) throws Exception {
		return this.query(null, emunQueryParameters);
	}

	public String query(final String path, final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters) throws Exception {
		return process(path, enumQueryParamsMapConvert(emunQueryParameters), ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
	}

	public String get() throws Exception {
		return this.get(null);
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

	public void delete() throws Exception {
		this.delete(null);
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

	public String paginate(final String path, Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters, final Integer pageOffset, final Integer pageSize) throws Exception {
		Map<String, String> paramMap = enumQueryParamsMapConvert(emunQueryParameters);
		if (pageOffset != null) {
			paramMap.put(PagingAndSortingParameter.start.name(), String.valueOf(pageOffset));
		}
		if (pageSize != null) {
			paramMap.put(PagingAndSortingParameter.size.name(), String.valueOf(pageSize));
		}
		return process(path, paramMap, ClientResponse.Status.OK.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(final WebResource webResource) {
				return webResource.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			}
		});
	}

	public String create(final String jsonBody) throws Exception {
		return this.create(null, jsonBody);
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

	public ClientResponse simpleCreate(final String jsonBody) {
		return this.simpleCreate(null, jsonBody);
	}

	public ClientResponse simpleCreate(final String path, final String jsonBody) {
		WebResource webResource = jerseyClient.resource(hostUri);
		if (!StringUtils.isEmpty(path)) {
			webResource = webResource.path(path);
		}
		return webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonBody);
	}

	public String update(final String updateJsonBody) throws Exception {
		return this.update(null, updateJsonBody);
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

	private Map<String, String> enumQueryParamsMapConvert(final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters) {
		Map<String, String> paramMap = Maps.<String, String> newHashMap();
		for (Map.Entry<? extends AbstractEnumQueryParameter, String> entry : emunQueryParameters.entrySet()) {
			paramMap.put(entry.getKey().name(), entry.getValue());
		}
		return paramMap;
	}
}
