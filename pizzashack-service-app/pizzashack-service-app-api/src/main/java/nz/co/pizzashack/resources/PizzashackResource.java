package nz.co.pizzashack.resources;

import static nz.co.pizzashack.GenericAPIUtils.buildResponseOnException;

import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/pizzashack")
public class PizzashackResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackResource.class);

	@Inject
	private PizzashackRepository pizzashackRepository;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@GET
	public Response doGet() {
		return Response.ok("PizzashackResource API is available ...").type(MediaType.TEXT_PLAIN).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@Context final UriInfo uriInfo, Pizzashack addPizzashack) throws Exception {
		LOGGER.info("addPizzashack:{} ", addPizzashack);
		final String id = "P-" + UUID.randomUUID().toString();
		addPizzashack.setPizzashackId(id);
		try {
			pizzashackRepository.create(addPizzashack);
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
		return Response.created(uriInfo.getRequestUriBuilder().replacePath("/" + id).build()).entity(id).type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() throws Exception {
		LOGGER.info("list start..");
		final Set<Pizzashack> pizzashacks = pizzashackRepository.getAll();
		return Response.ok(jacksonObjectMapper.writeValueAsString(pizzashacks)).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/{pizzashackId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("pizzashackId") String pizzashackId) throws Exception {
		Pizzashack found = null;
		try {
			found = pizzashackRepository.getById(pizzashackId);
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
		return Response.ok(jacksonObjectMapper.writeValueAsString(found)).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@DELETE
	@Path("/{pizzashackId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteById(@PathParam("pizzashackId") String pizzashackId) throws Exception {
		try {
			pizzashackRepository.deleteById(pizzashackId);
		} catch (Exception e) {
			return buildResponseOnException(e);
		}
		return Response.noContent().build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Pizzashack updatePizzashack) throws Exception {
		try {
			pizzashackRepository.update(updatePizzashack);
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
		return Response.ok().build();
	}

}
