package nz.co.pizzashack.ds;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Role;

public interface RoleDS {
	Role create(Role addRole) throws Exception;

	void updateRole(String roleName, Role updateRole) throws Exception;

	void deleteRoleByName(String roleName) throws Exception;

	Set<Role> getAllRoles() throws Exception;

	Role getRoleByName(String roleName) throws NotFoundException;

	Page<Role> paginateAll(Integer pageOffset, Integer pageSize) throws Exception;
}
