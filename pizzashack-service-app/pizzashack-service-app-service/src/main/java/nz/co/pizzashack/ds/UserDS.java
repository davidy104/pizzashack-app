package nz.co.pizzashack.ds;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.model.User;

public interface UserDS {

	User createUser(User addUser, String... roleNames) throws Exception;

	Set<User> getAllUsers() throws Exception;

	User getUserByName(String userName) throws NotFoundException;

	boolean login(String userName, String password);

	void updateUser(String userName, User updateUser, Set<String> newSelectedRoleNames) throws Exception;

	void deleteUserByName(String userName) throws Exception;

	Page<User> paginateAllUsers(Integer pageOffset, Integer pageSize) throws Exception;

	void deleteCommentByUserName(String userName) throws Exception;

	Set<PizzashackComment> getAllByUserName(String userName) throws Exception;

	Set<Role> getRolesByUserName(String userName);

	void gruntRoles(String userName, String... roleNames) throws Exception;

	void revokeRole(String userName, String roleName) throws Exception;
}
