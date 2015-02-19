package nz.co.pizzashack.repository;

import java.util.Date;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.User;

public interface UserRepository {

	String create(User addUser) throws Exception;

	Set<User> getAll() throws Exception;

	User getByName(String userName) throws NotFoundException;

	User getByNameAndPwd(String userName, String password) throws NotFoundException;

	void update(User updateUser) throws Exception;

	void deleteByName(String userName) throws Exception;

	Page<User> paginateAll(int pageOffset, int pageSize) throws Exception;

	Page<User> paginateByRole(int pageOffset, int pageSize, String roleName) throws Exception;

	Page<User> paginateByUserName(int pageOffset, int pageSize, String userName) throws Exception;

	void gruntRole(String userNodeUri, String roleNodeUri, Date createTime) throws Exception;

	void revokeRole(String userName, String roleName) throws Exception;
}
