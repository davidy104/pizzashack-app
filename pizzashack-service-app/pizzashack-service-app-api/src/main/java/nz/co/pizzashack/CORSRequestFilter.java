package nz.co.pizzashack;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class CORSRequestFilter implements ContainerRequestFilter {

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		MultivaluedMap<String, String> headers = request.getRequestHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return request;
	}

}
