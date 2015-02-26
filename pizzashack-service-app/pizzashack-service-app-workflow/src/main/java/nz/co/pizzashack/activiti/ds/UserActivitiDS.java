package nz.co.pizzashack.activiti.ds;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.User;
import nz.co.pizzashack.model.workflow.query.UserQueryParameter;

public interface UserActivitiDS {
	User getUserById(String userId) throws NotFoundException;

	Set<User> getUserByEmail(String email);

	Set<User> getUsersByName(String firstName, String lastName);

	User createUser(User addUser) throws Exception;

	User updateUser(String userId, User updateUser) throws Exception;

	void deleteUser(String userId) throws Exception;

	Page<User> paginatingUsers(Map<UserQueryParameter, String> userQueryParameters, Integer pageOffset, Integer pageSize);

	void updateUsersPicture(String userId, InputStream picStream) throws Exception;
}
