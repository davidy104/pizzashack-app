package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.test.TestUtils.initPizzashackFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;
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
public class PizzashackRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryIntegrationTest.class);
	private static Set<Pizzashack> initialPizzashacks = Collections.<Pizzashack> emptySet();
	private final static String PIZZASHACK_INIT_FILE = "pizzashack-data.txt";

	private Set<String> initPizzashackNodeUris = Sets.<String> newHashSet();

	@Inject
	private PizzashackRepository pizzashackRepository;

	@BeforeClass
	public static void setUp() {
		initialPizzashacks = initPizzashackFromFile(PIZZASHACK_INIT_FILE);
		assertNotNull(initialPizzashacks);
		assertEquals(initialPizzashacks.size(), 10);
	}

	@Before
	public void init() throws Exception {
		for (final Pizzashack pizzashack : initialPizzashacks) {
			final String nodeUri = pizzashackRepository.create(pizzashack);
			LOGGER.info("----createdNodeUri:{} ", nodeUri);
			initPizzashackNodeUris.add(nodeUri);
		}
	}

	@After
	public void tearDown() throws Exception {
		for (final Pizzashack pizzashack : initialPizzashacks) {
			pizzashackRepository.deleteById(pizzashack.getPizzashackId());
		}
	}

	@Test
	public void testCRUD() throws Exception {
		final String pizzashackId = UUID.randomUUID().toString();
		final String nodeUri = pizzashackRepository.create(new Pizzashack.Builder().pizzaName("testPizzaname").pizzashackId(pizzashackId).description("testDesc").build());
		LOGGER.info("nodeUri:{} ", nodeUri);

		Pizzashack found = pizzashackRepository.getById(pizzashackId);
		LOGGER.info("found---------------:{} ", found);

		found.setDescription("updateDesc");
		found.setPizzaName("updatePizzaname");
		found.setIcon("updateIcon");
		pizzashackRepository.updateById(pizzashackId, found);

		found = pizzashackRepository.getById(pizzashackId);
		LOGGER.info("after update---------------:{} ", found);

		pizzashackRepository.deleteById(pizzashackId);
	}

	@Test
	public void testGetAll() throws Exception {
		Set<Pizzashack> allPizzashacks = pizzashackRepository.getAll();
		assertEquals(allPizzashacks.size(), 10);
		for (final Pizzashack pizzashack : allPizzashacks) {
			LOGGER.info("pizzashack name:{} ", pizzashack.getClass().getSimpleName());
			LOGGER.info("pizzashack:{} ", pizzashack);

		}
	}

	@Test
	public void testPagination() throws Exception {
		Page<Pizzashack> page = pizzashackRepository.paginateAll(0, 3);
		LOGGER.info("---------page:{} ", page);

		for (final Pizzashack pizzashack : page.getContent()) {
			LOGGER.info("------------pizzashack:{} ", pizzashack);
		}
	}

}
