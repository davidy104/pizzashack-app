package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Role;

public interface RoleRepository {

	String create(Role addRole) throws Exception;

	void update(Role updateRole) throws Exception;

	void deleteByName(String roleName) throws Exception;

	Set<Role> getAll() throws Exception;

	Role getByName(String roleName) throws NotFoundException;

	Page<Role> paginateAll(int pageOffset, int pageSize) throws Exception;

	Set<Role> getByUserName(String userName);
}
