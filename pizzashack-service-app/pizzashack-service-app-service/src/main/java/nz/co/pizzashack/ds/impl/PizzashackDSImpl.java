package nz.co.pizzashack.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PizzashackDSImpl implements PizzashackDS {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackDSImpl.class);

	@Produce(uri = "direct:ImageToS3")
	private ProducerTemplate producerTemplate;

	@Inject
	private PizzashackRepository pizzashackRepository;

	private final static String IMAGE_PATH = "/image/pizza/";

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	@Inject
	private AmazonS3 amazonS3;

	@Override
	public String createPizzashack(Pizzashack addPizzashack, final String imageName, final InputStream imageStream) throws Exception {
		checkArgument(addPizzashack != null, "addPizzashack can not be null");
		String id = addPizzashack.getPizzashackId();
		id = id == null ? "PIZZA-" + UUID.randomUUID().toString() : id;
		addPizzashack.setPizzashackId(id);
		pizzashackRepository.create(addPizzashack);
		if (!StringUtils.isEmpty(imageName) && imageStream != null) {
			producerTemplate.sendBodyAndProperty(imageStream, "outputPath", IMAGE_PATH + imageName);
		}
		return id;
	}

	@Override
	public S3Object loadImageFromS3(final String imageName) throws Exception {
		checkArgument(!StringUtils.isEmpty(imageName), "imageName can not be null");
		final String key = IMAGE_PATH + imageName;
		return amazonS3.getObject(new GetObjectRequest(awsS3Bucket, key));
	}

	@Override
	public void deleteById(final String pizzashackId) throws Exception {
		checkArgument(pizzashackId != null, "pizzashackId can not be null");
		final Pizzashack found = pizzashackRepository.getById(pizzashackId);
		final String icon = found.getIcon();
		LOGGER.info("found icone:{}", icon);
		if (!StringUtils.isEmpty(icon)) {
			final String key = IMAGE_PATH + icon;
			amazonS3.deleteObject(awsS3Bucket, key);
		}
		pizzashackRepository.deleteById(pizzashackId);
	}

	@Override
	public Pizzashack getPizzashackById(final String pizzashackId) throws NotFoundException {
		checkArgument(pizzashackId != null, "pizzashackId can not be null");
		return pizzashackRepository.getById(pizzashackId);
	}

	@Override
	public void updatePizzashack(final String pizzashackId, Pizzashack updatePizzashack) throws Exception {
		checkArgument(pizzashackId != null, "pizzashackId can not be null");
		checkArgument(updatePizzashack != null, "updatePizzashack can not be null");
		pizzashackRepository.getById(pizzashackId);
		updatePizzashack.setPizzashackId(pizzashackId);
		pizzashackRepository.update(updatePizzashack);
	}

	@Override
	public Set<Pizzashack> getAllPizzashack() {
		Set<Pizzashack> resultSet = Collections.<Pizzashack> emptySet();
		try {
			resultSet = pizzashackRepository.getAll();
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		return resultSet;
	}

	@Override
	public Page<Pizzashack> paginatePizzashack(final int pageOffset, final int pageSize) {
		Page<Pizzashack> page = null;
		try {
			page = pizzashackRepository.paginateAll(pageOffset, pageSize);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		return page;
	}

}
