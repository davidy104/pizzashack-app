package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.PizzashackInitUtils.initUserFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.RepositoryModule;
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

import com.google.common.collect.Sets;
import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, RepositoryModule.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryIntegrationTest.class);
	private static Set<User> initialUsers = Collections.<User> emptySet();
	private final static String NOT_EXIST_ID = "not exist id";
	private Set<String> initUserNodeUris = Sets.<String> newHashSet();

	@Inject
	private UserRepository userRepository;

	@BeforeClass
	public static void setUp() throws Exception {
		initialUsers = initUserFromFile();
		assertNotNull(initialUsers);
		assertEquals(initialUsers.size(), 3);
	}

	@Before
	public void init() throws Exception {
		for (final User user : initialUsers) {
			final String nodeUri = userRepository.create(user);
			LOGGER.info("----userName:{} ", user.getUserName());
			initUserNodeUris.add(nodeUri);
		}
	}

	@After
	public void tearDown() throws Exception {
		for (final User user : initialUsers) {
			userRepository.deleteByName(user.getUserName());
		}
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
		User testDuplicated = new User.Builder().userName("david").build();
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
		assertEquals(3, userList.size());
	}
}
