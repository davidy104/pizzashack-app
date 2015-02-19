package nz.co.pizzashack.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Date;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.RoleRepository;
import nz.co.pizzashack.repository.UserRepository;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class UserDSImpl implements UserDS {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDSImpl.class);

	@Inject
	private UserRepository userRepository;

	@Inject
	private PizzashackCommentRepository pizzashackCommentRepository;

	@Inject
	private RoleRepository roleRepository;

	@Override
	public User createUser(User addUser, String... roleNames) throws Exception {
		checkArgument(addUser != null, "addUser can not be null");
		final String nodeUri = userRepository.create(addUser);
		addUser.setNodeUri(nodeUri);
		this.doGruntRoles(nodeUri, roleNames);
		return addUser;
	}

	@Override
	public Set<User> getAllUsers() throws Exception {
		return userRepository.getAll();
	}

	@Override
	public User getUserByName(final String userName) throws NotFoundException {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		User user = userRepository.getByName(userName);
		user.setRoles(roleRepository.getByUserName(userName));
		return user;
	}

	@Override
	public boolean login(final String userName, final String password) {
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
	public void updateUser(final String userName, User updateUser, Set<String> newSelectedRoleNames) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		checkArgument(updateUser != null, "updateUser can not be null");
		updateUser.setUserName(userName);
		userRepository.update(updateUser);
		if (newSelectedRoleNames != null && !newSelectedRoleNames.isEmpty()) {
			this.updateUsersRoles(userName, newSelectedRoleNames);
		}
	}

	private void updateUsersRoles(final String userName, Set<String> newSelectedRoleNames) throws Exception {
		Set<String> newRoles = Sets.<String> newHashSet();
		Set<String> removeRoles = Sets.<String> newHashSet();
		Set<String> currentRoles = Sets.<String> newHashSet();
		Set<Role> currentRoleSet = roleRepository.getByUserName(userName);
		for (final Role currentRole : currentRoleSet) {
			currentRoles.add(currentRole.getRoleName());
		}

		final Set<String> stillNeedRoles = Sets.intersection(newSelectedRoleNames, currentRoles);
		if (stillNeedRoles.isEmpty()) {
			newRoles = newSelectedRoleNames;
			removeRoles = currentRoles;
		}

		newRoles = ImmutableSet.copyOf(Collections2.filter(newSelectedRoleNames, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return !stillNeedRoles.contains(input.toUpperCase());
			}
		}));

		removeRoles = ImmutableSet.copyOf(Collections2.filter(currentRoles, new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return !stillNeedRoles.contains(input.toUpperCase());
			}
		}));

		for (final String removeRole : removeRoles) {
			this.revokeRole(userName, removeRole);
		}
		this.gruntRoles(userName, (String[]) newRoles.toArray());
	}

	@Override
	public void deleteUserByName(final String userName) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		userRepository.deleteByName(userName);
	}

	@Override
	public Page<User> paginateAllUsers(Integer pageOffset, Integer pageSize) throws Exception {
		pageOffset = pageOffset == null ? 0 : pageOffset;
		pageSize = pageSize == null ? 3 : pageSize;
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
		return pizzashackCommentRepository.getAllByUserName(userName);
	}

	@Override
	public Set<Role> getRolesByUserName(String userName) {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		return roleRepository.getByUserName(userName);
	}

	@Override
	public void gruntRoles(final String userName, final String... roleNames) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		final User foundUser = userRepository.getByName(userName);
		this.doGruntRoles(foundUser.getNodeUri(), roleNames);
	}

	private void doGruntRoles(final String userNodeUri, final String... roleNames) throws Exception {
		Role foundRole = null;
		for (final String roleName : roleNames) {
			try {
				foundRole = roleRepository.getByName(roleName);
			} catch (final NotFoundException e) {
				continue;
			}
			if (foundRole != null) {
				final String roleNodeUri = foundRole.getNodeUri();
				userRepository.gruntRole(userNodeUri, roleNodeUri, new Date());
			}
		}
	}

	@Override
	public void revokeRole(final String userName, final String roleName) throws Exception {
		checkArgument(!StringUtils.isEmpty(userName), "userName can not be null");
		checkArgument(!StringUtils.isEmpty(roleName), "roleName can not be null");
		userRepository.revokeRole(userName, roleName);
	}

}
