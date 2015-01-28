package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.User;

public interface UserRepository {

	String create(User addUser) throws Exception;

	Set<User> getAll() throws Exception;

	User getByName(String userName) throws Exception;

	User getByNameAndPwd(String userName, String password) throws Exception;

	void updateByName(String userName, User updateUser) throws Exception;

	void deleteByName(String userName) throws Exception;
}
