package nz.co.pizzashack.test.workflow;

import static nz.co.pizzashack.test.workflow.WorkflowTestUtil.initTestUsers;

import java.util.Set;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.activiti.WorkflowModule;
import nz.co.pizzashack.activiti.ds.GroupActivitiDS;
import nz.co.pizzashack.activiti.ds.UserActivitiDS;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.workflow.User;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.After;
import org.junit.Before;
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

	@BeforeClass
	public static void setup() throws Exception {
		initTestUsers = initTestUsers();
	}

	@Before
	public void init() throws Exception {
		for (final User addUser : initTestUsers) {
			testUsers.add(userActivitiDS.createUser(addUser));
		}
	}

	@After
	public void tearDown() throws Exception {
		for (final User addUser : testUsers) {
			userActivitiDS.deleteUser(addUser.getId());
		}
	}

	@Test
	public void testUserCRUD() {
		
	}

}
