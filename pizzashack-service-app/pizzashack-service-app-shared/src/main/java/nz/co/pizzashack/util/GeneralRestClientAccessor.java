package nz.co.pizzashack.util;

import static com.google.common.base.Preconditions.checkArgument;
import static nz.co.pizzashack.util.JerseyClientUtil.getResponsePayload;

import java.util.Map;

import nz.co.pizzashack.AbstractEnumQueryParameter;
import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

public class GeneralRestClientAccessor {

	protected Client jerseyClient;
	protected String hostUri;

	public GeneralRestClientAccessor(final Client jerseyClient, final String hostUri) {
		this.jerseyClient = jerseyClient;
		this.hostUri = hostUri;
	}

	protected String process(String path, final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters, final int expectedStatus, final RestClientExecuteCallback restClientCallback, final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(path), "path can not be null");
		checkArgument(restClientCallback != null, "restClientCallback can not be null");
		WebResource webResource = jerseyClient.resource(hostUri);
		if (!MapUtils.isEmpty(emunQueryParameters)) {
			for (Map.Entry<? extends AbstractEnumQueryParameter, String> entry : emunQueryParameters.entrySet()) {
				webResource = webResource.queryParam(entry.getKey().name(), entry.getValue());
			}
		}
		if (path.lastIndexOf('/') == path.length() - 1) {
			path = path.substring(0, path.lastIndexOf('/'));
		}
		webResource = webResource.path(path);
		final ClientResponse clientResponse = restClientCallback.execute(webResource);
		final Status status = (Status) (clientResponse.getStatusInfo());
		final String respStr = getResponsePayload(clientResponse);
		if (status.getStatusCode() != expectedStatus) {
			this.doErrorHandle(status, respStr, customErrorHandlers);
		}
		return respStr;
	}

	protected String process(final String path, final int expectedStatus, final RestClientExecuteCallback restClientCallback, final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(path), "path can not be null");
		checkArgument(restClientCallback != null, "restClientCallback can not be null");
		WebResource webResource = jerseyClient.resource(hostUri).path(path);
		final ClientResponse clientResponse = restClientCallback.execute(webResource);
		final Status status = (Status) (clientResponse.getStatusInfo());
		final String respStr = getResponsePayload(clientResponse);
		if (status.getStatusCode() != expectedStatus) {
			this.doErrorHandle(status, respStr, customErrorHandlers);
		}
		return respStr;
	}

	private void doErrorHandle(final Status statusCode, final String responseString, final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		if (customErrorHandlers != null && customErrorHandlers.length > 0) {
			for (RestClientCustomErrorHandler restClientCustomErrorHandler : customErrorHandlers) {
				restClientCustomErrorHandler.handle(statusCode.getStatusCode(), responseString);
			}
		} else {
			switch (statusCode) {
			case NOT_FOUND:
				throw new NotFoundException(responseString);
			case CONFLICT:
				throw new ConflictException(responseString);
			default:
				throw new IllegalStateException(responseString);
			}
		}
	}
}
