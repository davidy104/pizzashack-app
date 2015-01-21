package nz.co.pizzashack.util;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public interface RestClientExecuteCallback {
	ClientResponse execute(WebResource webResource);
}
