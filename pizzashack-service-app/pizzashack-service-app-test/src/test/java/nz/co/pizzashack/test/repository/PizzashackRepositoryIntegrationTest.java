package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
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
	private final static String NOT_EXIST_ID = "not exist id";

	private Set<String> initPizzashackNodeUris = Sets.<String> newHashSet();

	@Inject
	private PizzashackRepository pizzashackRepository;

	@BeforeClass
	public static void setUp() throws Exception {
		initialPizzashacks = initPizzashackFromFile();
		assertNotNull(initialPizzashacks);
		assertEquals(initialPizzashacks.size(), 10);
	}

	@Before
	public void init() throws Exception {
		for (final Pizzashack pizzashack : initialPizzashacks) {
			final String nodeUri = pizzashackRepository.create(pizzashack);
			LOGGER.info("----id:{} ", pizzashack.getPizzashackId());
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
		final String duplicatedId = "P-fea77b62-2e9c-43b5-9345-6b5922e37412";
		pizzashackRepository.create(new Pizzashack.Builder().pizzaName("testConflictPizzaname").pizzashackId(duplicatedId).description("testConflictPizzadesc").build());
	}

	@Test
	public void testGetAll() throws Exception {
		Set<Pizzashack> allPizzashacks = pizzashackRepository.getAll();
		assertNotNull(allPizzashacks);
		assertFalse(allPizzashacks.isEmpty());
		// assertEquals(allPizzashacks.size(), 10);
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
		Page<Pizzashack> page = pizzashackRepository.paginateAll(0, 3);
		LOGGER.info("---------page:{} ", page);

		for (final Pizzashack pizzashack : page.getContent()) {
			LOGGER.info("------------pizzashack:{} ", pizzashack);
		}
	}

}
