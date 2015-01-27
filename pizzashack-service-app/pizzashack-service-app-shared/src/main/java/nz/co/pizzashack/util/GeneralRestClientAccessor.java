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

/**
 * @author Davidy
 *
 */
public class GeneralRestClientAccessor {

	protected Client jerseyClient;
	protected String hostUri;

	public GeneralRestClientAccessor(final Client jerseyClient, final String hostUri) {
		this.jerseyClient = jerseyClient;
		this.hostUri = hostUri;
	}

	public String process(String path, final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters, final int expectedStatus,
			final RestClientExecuteCallback restClientCallback, final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(path), "path can not be null");
		return this.doProcess(hostUri, path, emunQueryParameters, expectedStatus, restClientCallback, customErrorHandlers);
	}

	public String doProcess(final String resource, String path, final Map<? extends AbstractEnumQueryParameter, String> emunQueryParameters, final int expectedStatus,
			final RestClientExecuteCallback restClientCallback, final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(resource), "resource can not be null");
		checkArgument(restClientCallback != null, "restClientCallback can not be null");
		WebResource webResource = jerseyClient.resource(resource);
		if (!MapUtils.isEmpty(emunQueryParameters)) {
			for (Map.Entry<? extends AbstractEnumQueryParameter, String> entry : emunQueryParameters.entrySet()) {
				webResource = webResource.queryParam(entry.getKey().name(), entry.getValue());
			}
		}
		if (!StringUtils.isEmpty(path)) {
			if (path.lastIndexOf('/') == path.length() - 1) {
				path = path.substring(0, path.lastIndexOf('/'));
			}
			webResource = webResource.path(path);
		}

		final ClientResponse clientResponse = restClientCallback.execute(webResource);
		final int statusCode = clientResponse.getStatusInfo().getStatusCode();
		final String respStr = getResponsePayload(clientResponse);
		if (statusCode != expectedStatus) {
			this.doErrorHandle(statusCode, respStr, customErrorHandlers);
		}
		return respStr;
	}

	public String process(final String path, final int expectedStatus, final RestClientExecuteCallback restClientCallback,
			final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(path), "path can not be null");
		return this.doProcess(hostUri, path, expectedStatus, restClientCallback, customErrorHandlers);
	}

	public String doProcess(String resource, String path, final int expectedStatus, final RestClientExecuteCallback restClientCallback,
			final RestClientCustomErrorHandler... customErrorHandlers) throws Exception {
		checkArgument(!StringUtils.isEmpty(resource), "resource can not be null");
		checkArgument(restClientCallback != null, "restClientCallback can not be null");
		WebResource webResource = jerseyClient.resource(resource);

		if (!StringUtils.isEmpty(path)) {
			if (path.lastIndexOf('/') == path.length() - 1) {
				path = path.substring(0, path.lastIndexOf('/'));
			}
			webResource = webResource.path(path);
		}

		final ClientResponse clientResponse = restClientCallback.execute(webResource);
		final int statusCode = clientResponse.getStatusInfo().getStatusCode();
		final String respStr = getResponsePayload(clientResponse);
		if (statusCode != expectedStatus) {
			this.doErrorHandle(statusCode, respStr, customErrorHandlers);
		}
		return respStr;
	}

	private void doErrorHandle(final int statusCode, final String responseString, final RestClientCustomErrorHandler... customErrorHandlers)
			throws Exception {
		if (customErrorHandlers != null && customErrorHandlers.length > 0) {
			for (final RestClientCustomErrorHandler restClientCustomErrorHandler : customErrorHandlers) {
				restClientCustomErrorHandler.handle(statusCode, responseString);
			}
		} else {
			if (statusCode == Status.NOT_FOUND.getStatusCode()) {
				throw new NotFoundException(responseString);
			} else if (statusCode == Status.CONFLICT.getStatusCode()) {
				throw new ConflictException(responseString);
			} else {
				throw new IllegalStateException(responseString);
			}
		}
	}

	public Client getJerseyClient() {
		return jerseyClient;
	}

	public String getHostUri() {
		return hostUri;
	}

}
