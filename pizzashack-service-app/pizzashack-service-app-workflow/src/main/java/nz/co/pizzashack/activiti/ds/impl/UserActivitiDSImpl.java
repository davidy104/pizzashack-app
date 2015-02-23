package nz.co.pizzashack.activiti.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import nz.co.pizzashack.OperationType;
import nz.co.pizzashack.activiti.convert.UserConverter;
import nz.co.pizzashack.activiti.ds.UserActivitiDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.User;
import nz.co.pizzashack.model.workflow.query.UserQueryParameter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.util.RestClientExecuteCallback;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;

public class UserActivitiDSImpl implements UserActivitiDS {

	private final static String USER_PATH = "/identity/users/";

	@Inject
	@Named("activitiGeneralJsonRestClientAccessor")
	private GeneralJsonRestClientAccessor activitiGeneralJsonRestClientAccessor;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserActivitiDSImpl.class);

	@Inject
	private UserConverter userConverter;

	@Override
	public User getUserById(final String userId) throws Exception {
		checkArgument(!StringUtils.isEmpty(userId), "userId can not be null.");
		return userConverter.jsonToUserModel(activitiGeneralJsonRestClientAccessor.get(USER_PATH + userId));
	}

	@Override
	public Set<User> getUserByEmail(final String email) {
		checkArgument(!StringUtils.isEmpty(email), "email can not be null.");
		try {
			final String respString = activitiGeneralJsonRestClientAccessor.query(USER_PATH, Maps.newHashMap(new ImmutableMap.Builder<UserQueryParameter, String>()
					.put(UserQueryParameter.emailLike, email)
					.build()));
			return userConverter.jsonToUserModels(respString);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Set<User> getUsersByName(final String firstName, final String lastName) {
		try {
			final String respString = activitiGeneralJsonRestClientAccessor.query(USER_PATH, Maps.newHashMap(new ImmutableMap.Builder<UserQueryParameter, String>()
					.put(UserQueryParameter.firstName, firstName)
					.put(UserQueryParameter.lastName, lastName)
					.build()));
			return userConverter.jsonToUserModels(respString);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public User createUser(final User addUser) throws Exception {
		checkArgument(addUser != null, "addUser can not be null.");
		checkArgument(!StringUtils.isEmpty(addUser.getId()), "user id can not be null.");
		final String userCreationJson = userConverter.userModelToJson(addUser, OperationType.CREATION);
		return userConverter.jsonToUserModel(activitiGeneralJsonRestClientAccessor.create(USER_PATH, userCreationJson));
	}

	@Override
	public User updateUser(final String userId, final User updateUser) throws Exception {
		checkArgument(!StringUtils.isEmpty(userId), "user id can not be null.");
		final String userUpdateJson = userConverter.userModelToJson(updateUser, OperationType.UPDATE);
		return userConverter.jsonToUserModel(activitiGeneralJsonRestClientAccessor.update(USER_PATH + userId, userUpdateJson));
	}

	@Override
	public void deleteUser(final String userId) throws Exception {
		checkArgument(!StringUtils.isEmpty(userId), "user id can not be null.");
		activitiGeneralJsonRestClientAccessor.delete(USER_PATH + userId);
	}

	@Override
	public Page<User> paginatingUsers(final Map<UserQueryParameter, String> userQueryParameters, final Integer pageOffset, final Integer pageSize) {
		try {
			return userConverter.jsonToUserPage(activitiGeneralJsonRestClientAccessor.paginate(USER_PATH, userQueryParameters, pageOffset, pageSize));
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void updateUsersPicture(final String userId, final InputStream picStream) throws Exception {
		checkArgument(!StringUtils.isEmpty(userId), "user id can not be null.");
		checkArgument(picStream != null, "picStream can not be null.");
		final FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.bodyPart(new StreamDataBodyPart(userId + ".jpg", picStream));
		activitiGeneralJsonRestClientAccessor.process(USER_PATH + userId + "/picture", ClientResponse.Status.NO_CONTENT.getStatusCode(), new RestClientExecuteCallback() {
			@Override
			public ClientResponse execute(WebResource webResource) {
				return webResource.type(MediaType.MULTIPART_FORM_DATA)
						.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, multiPart);
			}
		});
	}

}
