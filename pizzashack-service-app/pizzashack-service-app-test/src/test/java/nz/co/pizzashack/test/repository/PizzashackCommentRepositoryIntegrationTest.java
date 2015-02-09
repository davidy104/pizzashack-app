package nz.co.pizzashack.test.repository;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;
import static nz.co.pizzashack.PizzashackInitUtils.initUserFromFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.PizzashackCommentType;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.repository.UserRepository;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, RepositoryModule.class })
public class PizzashackCommentRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackCommentRepositoryIntegrationTest.class);

	@Inject
	private PizzashackCommentRepository pizzashackCommentRepository;

	@Inject
	private PizzashackRepository pizzashackRepository;

	@Inject
	private UserRepository userRepository;

	private static Set<Pizzashack> initialPizzashacks = Collections.<Pizzashack> emptySet();
	private static Set<User> initialUsers = Collections.<User> emptySet();

	private Set<Pizzashack> initialedTestPizzashack = Sets.<Pizzashack> newHashSet();
	private Set<User> initialedTestUser = Sets.<User> newHashSet();

	private Set<String> commentUris = Sets.<String> newHashSet();

	private String testPizzashackId;
	private String testPizzashackNodeUri;

	private String testUserName;
	private String testUserNodeUri;

	private String withoutCommentPizzashackId;

	@BeforeClass
	public static void setUp() throws Exception {
		initialPizzashacks = initPizzashackFromFile();
		assertNotNull(initialPizzashacks);
		assertEquals(initialPizzashacks.size(), 10);

		initialUsers = initUserFromFile();
		assertNotNull(initialUsers);
		assertEquals(initialUsers.size(), 3);
	}

	@Before
	public void init() throws Exception {
		for (int i = 0; i < 3; i++) {
			Pizzashack pizzashack = (Pizzashack) initialPizzashacks.toArray()[i];
			final String nodeUri = pizzashackRepository.create(pizzashack);
			pizzashack.setNodeUri(nodeUri);
			if (i < 2) {
				initialedTestPizzashack.add(pizzashack);
			} else {
				withoutCommentPizzashackId = pizzashack.getPizzashackId();
			}
		}
		for (int i = 0; i < 2; i++) {
			User user = (User) initialUsers.toArray()[i];
			final String nodeUri = userRepository.create(user);
			user.setNodeUri(nodeUri);
			initialedTestUser.add(user);
		}

		Pizzashack pizzashack = (Pizzashack) initialedTestPizzashack.toArray()[0];
		testPizzashackNodeUri = pizzashack.getNodeUri();
		testPizzashackId = pizzashack.getPizzashackId();

		User testUser1 = (User) initialedTestUser.toArray()[0];
		testUserNodeUri = testUser1.getNodeUri();
		testUserName = testUser1.getUserName();

		User testUser2 = (User) initialedTestUser.toArray()[1];
		String user2NodeUri = testUser2.getNodeUri();

		this.commentUris.add(pizzashackCommentRepository.createPizzashackComment(testPizzashackNodeUri, testUserNodeUri, new PizzashackComment.Builder().commentType(PizzashackCommentType.LIKE)
				.createTime(new Date())
				.message("good !")
				.build()));

		this.commentUris.add(pizzashackCommentRepository.createPizzashackComment(testPizzashackNodeUri, user2NodeUri, new PizzashackComment.Builder().commentType(PizzashackCommentType.DISLIKE)
				.createTime(new Date())
				.message("bad !")
				.build()));

		pizzashack = (Pizzashack) initialedTestPizzashack.toArray()[1];
		this.commentUris.add(pizzashackCommentRepository.createPizzashackComment(pizzashack.getNodeUri(), testUserNodeUri, new PizzashackComment.Builder().commentType(PizzashackCommentType.DISLIKE)
				.createTime(new Date())
				.message("not good yet !")
				.build()));

		assertEquals(commentUris.size(), 3);
		assertNotNull(withoutCommentPizzashackId);
	}

	@After
	public void tearDown() throws Exception {
		for (final Pizzashack pizzashack : initialedTestPizzashack) {
			pizzashackCommentRepository.deleteCommentByPizzashackId(pizzashack.getPizzashackId());
			pizzashackRepository.deleteById(pizzashack.getPizzashackId());
		}
		for (final User user : initialedTestUser) {
			userRepository.deleteByName(user.getUserName());
		}
	}

	@Test
	public void testGetByUserName() throws Exception {
		Set<PizzashackComment> comments = pizzashackCommentRepository.getAllByUserName(testUserName);
		assertNotNull(comments);
		assertEquals(comments.size(), 2);
		for (final PizzashackComment comment : comments) {
			LOGGER.info("comment:{} ", comment);
		}
	}

	@Test
	public void testGetByPizzashackId() throws Exception {
		Set<PizzashackComment> comments = pizzashackCommentRepository.getAllByPizzashackId(testPizzashackId);
		assertEquals(comments.size(), 2);
		for (final PizzashackComment comment : comments) {
			LOGGER.info("comment:{} ", comment);
		}
	}

	@Test
	public void testGetByUserNameAndPizzashackId() throws Exception {
		LOGGER.info("------------------testGetByUserNameAndPizzashackId start-------------------");
		// PizzashackComment found =
		// pizzashackCommentRepository.getByPizzashackIdAndUserName(testPizzashackId,
		// testUserName);
		// assertNotNull(found);
		// LOGGER.info("found:{} ", found);

		PizzashackComment found = pizzashackCommentRepository.getByPizzashackIdAndUserName(withoutCommentPizzashackId, testUserName);
		LOGGER.info("withoutCommentPizzashack:{} ", found);
		LOGGER.info("------------------testGetByUserNameAndPizzashackId end-------------------");
	}

	@Test
	public void testCRUD() throws Exception {

	}
}
