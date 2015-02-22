package nz.co.pizzashack.workflow.ds.impl;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.model.workflow.query.UserQueryParameter;
import nz.co.pizzashack.workflow.ds.UserDS;

public class UserDSImpl implements UserDS {

	private final static String USER_PATH = "/identity/users/";

	@Override
	public User getUserById(final String userId) {
		return null;
	}

	@Override
	public Set<User> getUserByEmail(final String email) {
		return null;
	}

	@Override
	public Set<User> getUsersByName(String firstName, String lastName) {
		return null;
	}

	@Override
	public User createUser(User addUser) throws Exception {
		return null;
	}

	@Override
	public User updateUser(String userId, User updateUser) throws Exception {
		return null;
	}

	@Override
	public void deleteUser(String userId) throws Exception {

	}

	@Override
	public Page<User> paginatingUsers(Map<UserQueryParameter, String> userQueryParameters, Integer pageOffset, Integer pageSize) {
		return null;
	}

	@Override
	public void updateUsersPicture(String userId, InputStream picStream) throws Exception {

	}

}
