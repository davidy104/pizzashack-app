package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.test.TestUtils.initRoles;
import static nz.co.pizzashack.test.TestUtils.initUsers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.repository.RoleRepository;
import nz.co.pizzashack.repository.UserRepository;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, RepositoryModule.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryIntegrationTest.class);
	private Set<User> initialUsers = Sets.<User> newHashSet();
	private static Map<User, String> userMap = null;
	private static Set<Role> initialRoles = Collections.<Role> emptySet();
	private final static String NOT_EXIST_ID = "not exist id";

	@Inject
	private UserRepository userRepository;

	@Inject
	private RoleRepository roleRepository;

	private String developerRoleNodeUri;
	private String staffRoleNodeUri;

	@BeforeClass
	public static void setUp() throws Exception {
		userMap = initUsers();
		initialRoles = initRoles();
		assertEquals(initialRoles.size(), 2);
	}

	@Before
	public void init() throws Exception {
		for (final Role role : initialRoles) {
			String roleNodeUri = roleRepository.create(role);
			if (role.getRoleName().equals("staff")) {
				staffRoleNodeUri = roleNodeUri;
			} else {
				developerRoleNodeUri = roleNodeUri;
			}
		}
		for (final Map.Entry<User, String> entry : userMap.entrySet()) {
			final String rolesStr = entry.getValue();
			User user = entry.getKey();
			final String userNodeUri = userRepository.create(user);
			user.setNodeUri(userNodeUri);
			initialUsers.add(user);
			Iterable<String> roleIterable = Splitter.on(",").split(rolesStr);
			List<String> roleList = FluentIterable.<String> from(roleIterable).toList();
			for (final String roleName : roleList) {
				final Date createTime = new Date();
				if (roleName.equals("staff")) {
					userRepository.gruntRole(userNodeUri, staffRoleNodeUri, createTime);
				} else if (roleName.equals("developer")) {
					userRepository.gruntRole(userNodeUri, developerRoleNodeUri, createTime);
				}
			}
		}
	}

	@After
	public void tearDown() throws Exception {
		LOGGER.info("-----------------tearDown start--------------------");
		for (final User user : initialUsers) {
			userRepository.deleteByName(user.getUserName());
		}
		for (final Role role : initialRoles) {
			roleRepository.deleteByName(role.getRoleName());
		}
		LOGGER.info("-----------------tearDown end--------------------");
	}

	@Test
	public void testGetByName() throws Exception {
		final String testUser = "eric";
		final User user = userRepository.getByName(testUser);
		assertNotNull(user);
		LOGGER.info("found user:{} ", user);
	}

	@Test
	public void testCRUD() throws Exception {
		User testAdd = new User.Builder().userName("testUser").password("testPassword").createTime(new Date()).build();
		final String addedUri = userRepository.create(testAdd);
		LOGGER.info("addedUri:{} ", addedUri);
		testAdd = userRepository.getByName("testUser");
		assertNotNull(testAdd);
		assertEquals(testAdd.getUserName(), "testUser");
		assertEquals(testAdd.getPassword(), "testPassword");

		testAdd.setPassword("testUptPassword");
		userRepository.update(testAdd);
		testAdd = userRepository.getByName("testUser");
		assertNotNull(testAdd);
		assertEquals(testAdd.getUserName(), "testUser");
		assertEquals(testAdd.getPassword(), "testUptPassword");
		userRepository.deleteByName("testUser");
	}

	/**
	 * for david user, check init file
	 * 
	 * @throws Exception
	 */
	@Test(expected = ConflictException.class)
	public void testCreateConflictUser() throws Exception {
		User testDuplicated = new User.Builder().userName("james").build();
		userRepository.create(testDuplicated);
	}

	@Test(expected = NotFoundException.class)
	public void testGetNotFound() throws Exception {
		userRepository.getByName(NOT_EXIST_ID);
	}

	@Test
	public void testGetAll() throws Exception {
		Set<User> userList = userRepository.getAll();
		assertNotNull(userList);
		assertEquals(2, userList.size());
	}
}
