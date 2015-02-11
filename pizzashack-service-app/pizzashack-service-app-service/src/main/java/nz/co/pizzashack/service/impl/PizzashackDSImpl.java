package nz.co.pizzashack.service.impl;

import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.service.PizzashackDS;

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

	private final static String IMAGE_PATH = "/image/pizzashack/";

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	@Inject
	private AmazonS3 amazonS3;

	@Override
	public String createPizzashack(Pizzashack addPizzashack, final String imageName, final InputStream imageStream) throws Exception {
		final String id = "PIZZA-" + UUID.randomUUID().toString();
		addPizzashack.setPizzashackId(id);
		pizzashackRepository.create(addPizzashack);
		if (!StringUtils.isEmpty(imageName) && imageStream != null) {
			producerTemplate.sendBodyAndProperty(imageStream, "outputPath", IMAGE_PATH + imageName);
		}
		return id;
	}
	
	@Override
	public S3Object loadImageFromS3(final String imageName) throws Exception {
		final String key = IMAGE_PATH + imageName;
		return amazonS3.getObject(new GetObjectRequest(awsS3Bucket, key));
	}


	@Override
	public void deleteById(final String pizzashackId) throws Exception {
		final Pizzashack found = pizzashackRepository.getById(pizzashackId);
		final String icon = found.getIcon();
		if(!StringUtils.isEmpty(icon)){
			String key = IMAGE_PATH +"/"+ icon;
			amazonS3.deleteObject(awsS3Bucket, key);
		}
		pizzashackRepository.deleteById(pizzashackId);
	}

	@Override
	public Pizzashack getPizzashackById(final String pizzashackId) throws NotFoundException {
		return pizzashackRepository.getById(pizzashackId);
	}

	@Override
	public void updatePizzashack(final String pizzashackId, Pizzashack updatePizzashack) throws Exception {
	}

	@Override
	public Set<Pizzashack> getAllPizzashack() {
		return null;
	}

	@Override
	public Page<Pizzashack> paginatePizzashack(final int pageOffset,final int pageSize) {
		Page<Pizzashack> page = null;
		try {
			page =  pizzashackRepository.paginateAll(pageOffset, pageSize);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		return page;
	}

}
