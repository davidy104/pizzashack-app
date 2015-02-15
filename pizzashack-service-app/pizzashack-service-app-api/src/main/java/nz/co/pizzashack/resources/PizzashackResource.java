package nz.co.pizzashack.resources;

import static nz.co.pizzashack.GenericAPIUtils.buildResponseOnException;
import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.model.Page;
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

	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response create(@Context final UriInfo uriInfo, final MultipartFormDataInput input) throws Exception {
		LOGGER.info("create start.");
		String id = null;
		Map<String, String> requestFormMap = Maps.<String, String> newHashMap();
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		InputStream imageStream = null;
		try {
			for (final Map.Entry<String, List<InputPart>> entry : uploadForm.entrySet()) {
				final String key = entry.getKey();
				final InputPart inputPart = entry.getValue().get(0);
				if (key.equals("image")) {
					imageStream = inputPart.getBody(InputStream.class, null);
					LOGGER.info("available:{} ", imageStream.available());
				} else if (key.equals("model")) {
					requestFormMap = jacksonObjectMapper.readValue(inputPart.getBodyAsString(), Map.class);
				}
			}
			if (!requestFormMap.isEmpty()) {
				id = pizzashackDS.createPizzashack(this.buildPizzashackFromRequestMap(requestFormMap), requestFormMap.get("icon"), imageStream);
			}
		} catch (final Exception e) {
			return buildResponseOnException(e);
		} finally {
			if (imageStream != null) {
				imageStream.close();
			}
		}
		return Response.status(Response.Status.CREATED).entity(id).type(MediaType.TEXT_PLAIN).build();
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
	@Path("/page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response paginate(@QueryParam("pageOffset") int pageOffset, @QueryParam("pageSize") int pageSize, @QueryParam("pizzashackName") String pizzashackName) throws Exception {
		LOGGER.info("paginate start..");
		LOGGER.info("pageOffset:{} ", pageOffset);
		LOGGER.info("pageSize:{} ", pageSize);
		LOGGER.info("pizzashackName:{} ", pizzashackName);
		Page<Pizzashack> page = pizzashackDS.paginatePizzashackByName(pageOffset, pageSize, pizzashackName);
		LOGGER.info("page:{} ",page);
		return Response.ok(jacksonObjectMapper.writeValueAsString(page)).type(MediaType.APPLICATION_JSON)
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
		LOGGER.info("deleteById start:{} ", pizzashackId);
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
		try {
			Pizzashack pizzashack = new Pizzashack.Builder()
					.pizzaName(requestFormMap.get("pizzaName"))
					.description(requestFormMap.get("description"))
					.icon(requestFormMap.get("icon"))
					.build();

			if (requestFormMap.get("amount") != null) {
				pizzashack.setAmount(Integer.valueOf(requestFormMap.get("amount")));
			}

			if (requestFormMap.get("price") != null) {
				pizzashack.setPrice(new BigDecimal(requestFormMap.get("price")));
			}

			if (requestFormMap.get("createTime") != null) {
				final String createDateStr = requestFormMap.get("createTime");
				LOGGER.info("createDateStr:{} ", createDateStr);
				pizzashack.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", createDateStr));
			}
			return pizzashack;
		} catch (final Exception e) {
			throw new IllegalArgumentException(e);
		}
	}


}
