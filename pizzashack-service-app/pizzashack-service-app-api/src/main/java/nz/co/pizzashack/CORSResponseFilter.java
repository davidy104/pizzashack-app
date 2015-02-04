package nz.co.pizzashack;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSResponseFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		MultivaluedMap<String, Object> headers = response.getHttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return response;
	}

}
