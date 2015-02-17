package nz.co.pizzashack.ds;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.User;

public interface UserDS {

	User createUser(User addUser) throws Exception;

	Set<User> getAllUsers() throws Exception;

	User getUserByName(String userName) throws NotFoundException;

	boolean login(String userName, String password);

	void updateUser(String userName, User updateUser) throws Exception;

	void deleteUserByName(String userName) throws Exception;

	Page<User> paginateAllUsers(int pageOffset, int pageSize) throws Exception;

	void deleteCommentByUserName(String userName) throws Exception;

	Set<PizzashackComment> getAllByUserName(String userName) throws Exception;
}
