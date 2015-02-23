package nz.co.pizzashack.test.workflow;

import java.util.Set;

import nz.co.pizzashack.model.workflow.User;

import com.google.common.collect.Sets;

public class WorkflowTestUtil {

	public static Set<User> initTestUsers() {
		Set<User> users = Sets.<User> newHashSet();
		User testUser = new User.Builder()
				.email("test1Email")
				.firstName("test1FirstName")
				.lastName("test1LastName")
				.password("test1Pwd")
				.id("test1Id")
				.build();
		users.add(testUser);
		testUser = new User.Builder()
				.email("test2Email")
				.firstName("test2FirstName")
				.lastName("test2LastName")
				.password("test2Pwd")
				.id("test2Id")
				.build();
		users.add(testUser);
		return users;
	}
}
