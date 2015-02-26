package nz.co.pizzashack.test.workflow;

import static nz.co.pizzashack.test.workflow.WorkflowTestUtil.initTestUsers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.activiti.WorkflowModule;
import nz.co.pizzashack.activiti.ds.GroupActivitiDS;
import nz.co.pizzashack.activiti.ds.UserActivitiDS;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Group;
import nz.co.pizzashack.model.workflow.User;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, WorkflowModule.class })
public class IdentityIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdentityIntegrationTest.class);

	@Inject
	private UserActivitiDS userActivitiDS;

	@Inject
	private GroupActivitiDS groupActivitiDS;

	private User testUser;

	private static Set<User> initTestUsers;

	private Set<User> testUsers;

	private static final String TEST_SALES_GROUP = "sales";

	@BeforeClass
	public static void setup() throws Exception {
		initTestUsers = initTestUsers();
	}

	// @Before
	// public void init() throws Exception {
	// for (final User addUser : initTestUsers) {
	// testUsers.add(userActivitiDS.createUser(addUser));
	// }
	// }
	//
	// @After
	// public void tearDown() throws Exception {
	// for (final User addUser : testUsers) {
	// userActivitiDS.deleteUser(addUser.getId());
	// }
	// }

	@Test
	public void testPaginateUsers() {
		Page<User> page = userActivitiDS.paginatingUsers(null, 0, 5);
		assertNotNull(page);
		for (final User user : page.getContent()) {
			LOGGER.info("found user:{} ", user);
		}
	}

	@Test(expected = NotFoundException.class)
	public void testGetGroupByIdNotFound() throws Exception {
		groupActivitiDS.getGroupById("NotExistGroupId");
	}

	@Test
	public void testGroupCRUD() throws Exception {
		final String testGroupId = "testGroup";
		Group group = groupActivitiDS.createGroup(new Group.Builder()
				.id("testGroup")
				.name("testGroup")
				.type("test")
				.build());
		assertNotNull(group);
		LOGGER.info("after create group:{} ", group);
		group = groupActivitiDS.getGroupById(testGroupId);
		assertNotNull(group);
		LOGGER.info("after found group:{} ", group);
		groupActivitiDS.deleteGroup(testGroupId);
	}

	@Test
	public void testPaginateGroup() {
		Page<Group> page = groupActivitiDS.paginateGroup(null, 0, 5);
		assertEquals(page.getPageSize(), 5);
		assertEquals(page.getPageOffset(), 0);
		assertNotNull(page);
		LOGGER.info("page:{} ", page);
		Set<Group> groups = page.getContent();
		assertNotNull(groups);
		for (final Group group : groups) {
			LOGGER.info("found group:{} ", group);
		}
	}

}
