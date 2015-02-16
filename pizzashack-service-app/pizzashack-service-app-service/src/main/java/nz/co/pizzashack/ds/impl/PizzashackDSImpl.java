package nz.co.pizzashack.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.PizzashackCommentType;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.UserRepository;

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
	
	@Inject
	private PizzashackCommentRepository pizzashackCommentRepository;
	
	@Inject
	private UserRepository userRepository;

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
		checkArgument(!StringUtils.isEmpty(pizzashackId), "pizzashackId can not be null");
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
		checkArgument(!StringUtils.isEmpty(pizzashackId), "pizzashackId can not be null");
		return pizzashackRepository.getById(pizzashackId);
	}

	@Override
	public void updatePizzashack(final String pizzashackId, Pizzashack updatePizzashack) throws Exception {
		checkArgument(!StringUtils.isEmpty(pizzashackId), "pizzashackId can not be null");
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
	public Page<Pizzashack> paginatePizzashack(Integer pageOffset, Integer pageSize) {
		pageOffset = pageOffset == null ? 0 : pageOffset;
		pageSize = pageSize == null ? 3 : pageSize;
		Page<Pizzashack> page = null;
		try {
			page = pizzashackRepository.paginateAll(pageOffset, pageSize);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		return page;
	}

	@Override
	public Page<Pizzashack> paginatePizzashackByName(Integer pageOffset, Integer pageSize, final String pizzashackName) {
		Page<Pizzashack> page = null;
		pageOffset = pageOffset == null ? 0 : pageOffset;
		pageSize = pageSize == null ? 3 : pageSize;
		try {
			if (StringUtils.isEmpty(pizzashackName)) {
				page = pizzashackRepository.paginateAll(pageOffset, pageSize);
			} else {
				page = pizzashackRepository.paginateByName(pageOffset, pageSize, pizzashackName);
			}
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
		return page;
	}

	@Override
	public Long countCommentsByPizzashackId(final String pizzashackId,final PizzashackCommentType commentType) {
		checkArgument(!StringUtils.isEmpty(pizzashackId), "pizzashackId can not be null");
		checkArgument(commentType != null, "commentType can not be null");
		try {
			return pizzashackCommentRepository.countCommentsByPizzashackId(pizzashackId, commentType);
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String createPizzashackComment(final String pizzashackId,final String userName,final PizzashackComment comment) throws Exception {
		checkArgument(!StringUtils.isEmpty(pizzashackId), "pizzashackId can not be null");
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		checkArgument(comment != null, "comment can not be null");
		boolean commentExist = true;
		try {
			pizzashackCommentRepository.getByPizzashackIdAndUserName(pizzashackId, userName);
		} catch (final NotFoundException e) {
			commentExist = false;
		}
		if(commentExist){
			throw new ConflictException("User["+userName+"] already has comment on this Pizzashack["+pizzashackId+"].");
		}
		
		final Pizzashack foundPizzashack = pizzashackRepository.getById(pizzashackId);
		final User foundUser = userRepository.getByName(userName);
		return pizzashackCommentRepository.createPizzashackComment(foundPizzashack.getNodeUri(), foundUser.getNodeUri(), comment);
	}

	@Override
	public void deleteCommentByPizzashackId(final String pizzashackId) throws Exception {
		pizzashackCommentRepository.deleteCommentByPizzashackId(pizzashackId);
	}
	
}
