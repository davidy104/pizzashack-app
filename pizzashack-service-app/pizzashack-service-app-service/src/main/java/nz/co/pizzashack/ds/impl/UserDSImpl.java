package nz.co.pizzashack.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.UserRepository;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class UserDSImpl implements UserDS{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDSImpl.class);
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private PizzashackCommentRepository pizzashackCommentRepository;
	
	@Override
	public String createUser(final User addUser) throws Exception {
		checkArgument(addUser != null, "addUser can not be null");
		return userRepository.create(addUser);
	}

	@Override
	public Set<User> getAllUsers() throws Exception {
		return userRepository.getAll();
	}

	@Override
	public User getUserByName(final String userName) throws NotFoundException {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		return null;
	}

	@Override
	public boolean login(final String userName,final String password) {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		checkArgument(!StringUtils.isEmpty(password), "password can not be null");
		try {
			userRepository.getByNameAndPwd(userName, password);
		} catch (final NotFoundException e) {
			return false;
		}
		return true;
	}

	@Override
	public void updateUser(final String userName, User updateUser) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		checkArgument(updateUser != null, "updateUser can not be null");
		updateUser.setUserName(userName);
		userRepository.update(updateUser);
	}

	@Override
	public void deleteUserByName(final String userName) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		userRepository.deleteByName(userName);
	}

	@Override
	public Page<User> paginateAllUsers(final int pageOffset,final int pageSize) throws Exception {
		return userRepository.paginateAll(pageOffset, pageSize);
	}

	@Override
	public void deleteCommentByUserName(final String userName) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		pizzashackCommentRepository.deleteCommentByUserName(userName);
	}

	@Override
	public Set<PizzashackComment> getAllByUserName(final String userName) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		return null;
	}
	
	

}
