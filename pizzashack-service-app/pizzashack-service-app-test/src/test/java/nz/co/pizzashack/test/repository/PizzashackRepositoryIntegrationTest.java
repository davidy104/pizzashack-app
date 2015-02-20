package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.test.TestUtils.initPizzashacks;
import static nz.co.pizzashack.test.TestUtils.initUsers;
import static nz.co.pizzashack.util.GenericUtils.readClasspathFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.PizzashackRepository;
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

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, RepositoryModule.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PizzashackRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryIntegrationTest.class);
	private static Set<Pizzashack> initialPizzashacks = Collections.<Pizzashack> emptySet();
	private static Set<User> intialUsers = Sets.<User> newHashSet();
	private final static String NOT_EXIST_ID = "not exist id";
	private static final String delimiter = "||";
	private final static String VIEWED_FILE = "init/viewed-testdata.txt";

	private static List<String> initViewedFileContent;

	private Set<String> initPizzashackNodeUris = Sets.<String> newHashSet();

	@Inject
	private PizzashackRepository pizzashackRepository;

	@Inject
	private UserRepository userRepository;

	private static String TEST_USER_NAME = "james";
	private static String TEST_PIZZASHACK_ID = "PIZZA-8e1d96e7-fc83-4327-8038-631b2e1ac8d3";
	private static String TEST_NOT_VIEWED_PIZZASHACK_ID = "PIZZA-079f9223-ef66-498a-8175-d0cba1a98973";

	@BeforeClass
	public static void setUp() throws Exception {
		initialPizzashacks = initPizzashacks();
		Map<User, String> initUserMap = initUsers();
		for (Map.Entry<User, String> entry : initUserMap.entrySet()) {
			intialUsers.add(entry.getKey());
		}
		initViewedFileContent = readClasspathFile(VIEWED_FILE);
		assertNotNull(initialPizzashacks);
		assertEquals(initialPizzashacks.size(), 3);
		assertEquals(intialUsers.size(), 2);
	}

	@Before
	public void init() throws Exception {
		for (final Pizzashack pizzashack : initialPizzashacks) {
			final String nodeUri = pizzashackRepository.create(pizzashack);
			LOGGER.info("----id:{} ", pizzashack.getPizzashackId());
			initPizzashackNodeUris.add(nodeUri);
		}

		for (final User user : intialUsers) {
			userRepository.create(user);
		}
		doInitViewed();
	}

	private void doInitViewed() throws Exception {
		for (final String line : initViewedFileContent) {
			Iterable<String> values = Splitter.on(delimiter).split(line);
			final String pizzashackId = Iterables.get(values, 0);
			final Pizzashack pizzashack = pizzashackRepository.getById(pizzashackId);
			final User user = userRepository.getByName(Iterables.get(values, 1));
			pizzashackRepository.createView(pizzashack.getNodeUri(), user.getNodeUri(), new Date());
		}
	}

	@After
	public void tearDown() throws Exception {
		for (final String line : initViewedFileContent) {
			Iterable<String> values = Splitter.on(delimiter).split(line);
			final String pizzashackId = Iterables.get(values, 0);
			final String userName = Iterables.get(values, 1);
			pizzashackRepository.deleteViewed(pizzashackId, userName);
		}

		for (final Pizzashack pizzashack : initialPizzashacks) {
			pizzashackRepository.deleteById(pizzashack.getPizzashackId());
		}

		for (final User user : intialUsers) {
			userRepository.deleteByName(user.getUserName());
		}
	}

	@Test
	public void testCreateAndDeleteViewed() throws Exception {
		final Pizzashack foundPizza = pizzashackRepository.getById(TEST_PIZZASHACK_ID);
		final User foundUser = userRepository.getByName(TEST_USER_NAME);
		pizzashackRepository.createView(foundPizza.getNodeUri(), foundUser.getNodeUri(), new Date());
		Long count = pizzashackRepository.countViewed(TEST_PIZZASHACK_ID);
		assertEquals(count.longValue(), 2);

		pizzashackRepository.deleteViewed(TEST_PIZZASHACK_ID, TEST_USER_NAME);
		count = pizzashackRepository.countViewed(TEST_PIZZASHACK_ID);
		assertEquals(count.longValue(), 1);
	}

	@Test
	public void testGetViewed() throws Exception {
		Long count = pizzashackRepository.countViewed(TEST_PIZZASHACK_ID);
		assertEquals(count.longValue(), 1);
		count = pizzashackRepository.countViewed(TEST_NOT_VIEWED_PIZZASHACK_ID);
		assertEquals(count.longValue(), 0);
	}

	@Test
	public void testCRUD() throws Exception {
		final String pizzashackId = "P-" + UUID.randomUUID().toString();

		final String nodeUri = pizzashackRepository.create(new Pizzashack.Builder().pizzaName("testPizzaname").pizzashackId(pizzashackId).description("testDesc").build());
		LOGGER.info("nodeUri:{} ", nodeUri);

		Pizzashack found = pizzashackRepository.getById(pizzashackId);
		LOGGER.info("found---------------:{} ", found);
		assertNotNull(found);
		found.setDescription("updateDesc");
		found.setPizzaName("updatePizzaname");
		found.setIcon("updateIcon");
		pizzashackRepository.update(found);

		found = pizzashackRepository.getById(pizzashackId);
		assertNotNull(found);
		LOGGER.info("after update---------------:{} ", found);

		pizzashackRepository.deleteById(pizzashackId);
	}

	@Test(expected = ConflictException.class)
	public void testCreateConflict() throws Exception {
		pizzashackRepository.create(new Pizzashack.Builder().pizzaName("testConflictPizzaname").pizzashackId(TEST_PIZZASHACK_ID).description("testConflictPizzadesc").build());
	}

	@Test
	public void testGetAll() throws Exception {
		Set<Pizzashack> allPizzashacks = pizzashackRepository.getAll();
		assertNotNull(allPizzashacks);
		assertFalse(allPizzashacks.isEmpty());
		assertEquals(allPizzashacks.size(), 3);
		for (final Pizzashack pizzashack : allPizzashacks) {
			LOGGER.info("pizzashack name:{} ", pizzashack.getClass().getSimpleName());
			LOGGER.info("pizzashack:{} ", pizzashack);

		}
	}

	@Test(expected = NotFoundException.class)
	public void testGetNotFound() throws Exception {
		pizzashackRepository.getById(NOT_EXIST_ID);
	}

	@Test(expected = NotFoundException.class)
	public void testDeleteNotFound() throws Exception {
		pizzashackRepository.deleteById(NOT_EXIST_ID);
	}

	@Test
	public void testPagination() throws Exception {
		Page<Pizzashack> page = pizzashackRepository.paginateAll(0, 2);
		LOGGER.info("---------page:{} ", page);

		for (final Pizzashack pizzashack : page.getContent()) {
			LOGGER.info("------------pizzashack:{} ", pizzashack);
		}
	}

}
