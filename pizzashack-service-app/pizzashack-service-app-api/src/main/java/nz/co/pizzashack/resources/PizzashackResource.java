package nz.co.pizzashack.resources;

import static nz.co.pizzashack.GenericAPIUtils.buildResponseOnException;
import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.model.Pizzashack;

import org.apache.commons.io.input.ProxyInputStream;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/admin/pizzashack")
public class PizzashackResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackResource.class);

	@Inject
	private PizzashackDS pizzashackDS;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@GET
	public Response doGet() {
		return Response.ok("PizzashackResource API is available ...").type(MediaType.TEXT_PLAIN).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response create(@Context final UriInfo uriInfo, final MultipartFormDataInput input) throws Exception {
		LOGGER.info("create start.");
		String id = null;
		String imageName = null;
		Map<String, String> requestFormMap = Maps.<String, String> newHashMap();
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		InputStream imageStream = null;
		try {
			for (final Map.Entry<String, List<InputPart>> entry : uploadForm.entrySet()) {
				final String key = entry.getKey();
				final InputPart inputPart = entry.getValue().get(0);

				if (key.equals("image")) {
					imageStream = inputPart.getBody(InputStream.class, null);
				} else if (key.equals("imageName")) {
					imageName = inputPart.getBodyAsString();
				} else {
					requestFormMap.put(key, inputPart.getBodyAsString());
				}
			}
			if (!requestFormMap.isEmpty()) {
				id = pizzashackDS.createPizzashack(this.buildPizzashackFromRequestMap(requestFormMap), imageName, imageStream);
			}
		} catch (final Exception e) {
			return buildResponseOnException(e);
		} finally {
			if (imageStream != null) {
				imageStream.close();
			}
		}
		return Response.created(uriInfo.getRequestUriBuilder().replacePath("/" + id).build()).entity(id).type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() throws Exception {
		LOGGER.info("list start..");
		final Set<Pizzashack> pizzashacks = pizzashackDS.getAllPizzashack();
		return Response.ok(jacksonObjectMapper.writeValueAsString(pizzashacks)).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/{pizzashackId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@Context final UriInfo uriInfo, @PathParam("pizzashackId") String pizzashackId) throws Exception {
		Pizzashack found = null;
		try {
			found = pizzashackDS.getPizzashackById(pizzashackId);
			final String icon = found.getIcon();
			if (!StringUtils.isEmpty(icon)) {
				final String imageSrc = uriInfo.getAbsolutePathBuilder().replacePath("/pizzashackApp/admin/pizzashack/image/" + icon).build().toString();
				LOGGER.info("imageSrc:{} ", imageSrc);
				found.setIcon(imageSrc);
			}
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
		return Response.ok(jacksonObjectMapper.writeValueAsString(found)).type(MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/image/{image}")
	@Produces("image/png")
	public Response showImage(@PathParam("image") String image) {
		LOGGER.info("showImage start:{}", image);
		try {
			final S3Object s3Object = pizzashackDS.loadImageFromS3(image);
			LOGGER.info("s3Object:{}", s3Object);
			LOGGER.info("getContentType:{}", s3Object.getObjectMetadata().getContentType());
			LOGGER.info("available:{}", s3Object.getObjectContent().available());
			
			return Response.ok(new ProxyInputStream(s3Object.getObjectContent()) {
				@Override
				public void close() throws IOException {
					super.close();
					s3Object.close();
				}
			}, s3Object.getObjectMetadata().getContentType()).build();
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
	}

	@DELETE
	@Path("/{pizzashackId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteById(@PathParam("pizzashackId") String pizzashackId) throws Exception {
		try {
			pizzashackDS.deleteById(pizzashackId);
		} catch (Exception e) {
			return buildResponseOnException(e);
		}
		return Response.noContent().build();
	}

	@PUT
	@Path("/{pizzashackId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("pizzashackId") String pizzashackId, Pizzashack updatePizzashack) throws Exception {
		try {
			pizzashackDS.updatePizzashack(pizzashackId, updatePizzashack);
		} catch (final Exception e) {
			return buildResponseOnException(e);
		}
		return Response.ok().build();
	}

	private Pizzashack buildPizzashackFromRequestMap(final Map<String, String> requestFormMap) {
		Pizzashack pizzashack = new Pizzashack.Builder()
				.pizzaName(requestFormMap.get("name"))
				.description(requestFormMap.get("description"))
				.build();

		if (requestFormMap.get("amount") != null) {
			pizzashack.setAmount(Integer.valueOf(requestFormMap.get("amount")));
		}
		if (requestFormMap.get("createDate") != null) {
			pizzashack.setCreateTime(parseToDate(requestFormMap.get("createDate"), "yyyy-MM-dd hh:mm:ss"));
		}

		return pizzashack;
	}

	// @POST
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Response create(@Context final UriInfo uriInfo, Pizzashack
	// addPizzashack) throws Exception {
	// LOGGER.info("addPizzashack:{} ", addPizzashack);
	// final String id = "P-" + UUID.randomUUID().toString();
	// addPizzashack.setPizzashackId(id);
	// try {
	// pizzashackRepository.create(addPizzashack);
	// } catch (final Exception e) {
	// return buildResponseOnException(e);
	// }
	// return Response.created(uriInfo.getRequestUriBuilder().replacePath("/" +
	// id).build()).entity(id).type(MediaType.APPLICATION_JSON).build();
	// }

}
