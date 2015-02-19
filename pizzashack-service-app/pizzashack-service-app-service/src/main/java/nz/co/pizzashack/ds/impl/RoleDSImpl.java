package nz.co.pizzashack.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.RoleDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.repository.RoleRepository;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class RoleDSImpl implements RoleDS {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleDSImpl.class);

	@Inject
	private RoleRepository roleRepository;

	@Override
	public Role create(Role addRole) throws Exception {
		checkArgument(addRole != null, "addRole can not be null");
		final String nodeUri = roleRepository.create(addRole);
		addRole.setNodeUri(nodeUri);
		return addRole;
	}

	@Override
	public void updateRole(String roleName, Role updateRole) throws Exception {
		checkArgument(!StringUtils.isEmpty(roleName), "roleName can not be null");
		checkArgument(updateRole != null, "updateRole can not be null");
		updateRole.setRoleName(roleName);
		roleRepository.update(updateRole);
	}

	@Override
	public void deleteRoleByName(String roleName) throws Exception {
		checkArgument(!StringUtils.isEmpty(roleName), "roleName can not be null");
		roleRepository.deleteByName(roleName);
	}

	@Override
	public Set<Role> getAllRoles() throws Exception {
		return roleRepository.getAll();
	}

	@Override
	public Role getRoleByName(String roleName) throws NotFoundException {
		checkArgument(!StringUtils.isEmpty(roleName), "roleName can not be null");
		return roleRepository.getByName(roleName);
	}

	@Override
	public Page<Role> paginateAll(Integer pageOffset, Integer pageSize) throws Exception {
		pageOffset = pageOffset == null ? 0 : pageOffset;
		pageSize = pageSize == null ? 3 : pageSize;
		return roleRepository.paginateAll(pageOffset, pageSize);
	}

}
