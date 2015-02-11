package nz.co.pizzashack;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class GenericAPIUtils {

	public static Response buildResponseOnException(final Throwable exception) {
		if (exception instanceof NotFoundException) {
			return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof ConflictException) {
			return Response.status(Status.CONFLICT).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
		} else if (exception instanceof IllegalArgumentException) {
			return Response.status(Status.BAD_REQUEST).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
		} else {
			throw new RuntimeException(exception);
		}
	}
}
