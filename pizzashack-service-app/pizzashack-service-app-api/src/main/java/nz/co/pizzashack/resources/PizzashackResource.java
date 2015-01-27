package nz.co.pizzashack.resources;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class PizzashackResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackResource.class);

	@Inject
	private PizzashackRepository pizzashackRepository;

	@GET
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() throws Exception {
		LOGGER.info("list start..");
		final Set<Pizzashack> pizzashacks = pizzashackRepository.getAll();
		return Response.ok(pizzashacks).type(MediaType.APPLICATION_JSON).build();
	}
}
