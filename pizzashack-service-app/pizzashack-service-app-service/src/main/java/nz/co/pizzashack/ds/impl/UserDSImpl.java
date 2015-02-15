package nz.co.pizzashack.ds.impl;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.User;

public class UserDSImpl implements UserDS{

	@Override
	public String createUser(User addUser) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<User> getAllUsers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByName(String userName) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean login(String userName, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateUser(String userName, User updateUser) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserByName(String userName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<User> paginateAllUsers(int pageOffset, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
