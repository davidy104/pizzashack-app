package nz.co.pizzashack.test;

import static nz.co.pizzashack.util.GenericUtils.readClasspathFile;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.model.User;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class TestUtils {
	private static final String delimiter = "||";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static final String TEST_PIZZA_DATA_FILE = "init/pizzashack-testdata.txt";
	private static final String TEST_USER_DATA_FILE = "init/user-testdata.txt";
	private final static String TEST_PIZZASHACK_COMMENTS_FILE = "init/comments-testdata.txt";
	private final static String TEST_VIEWED_FILE = "init/viewed-testdata.txt";
	private final static String TEST_ROLE_DATA_FILE = "init/role-testdata.txt";

	public static Set<Pizzashack> initPizzashackFromFile() throws Exception {
		Set<Pizzashack> pizzashackSet = Sets.<Pizzashack> newHashSet();
		List<String> lines = readClasspathFile(TEST_PIZZA_DATA_FILE);
		for (final String line : lines) {
			Iterable<String> values = Splitter.on(delimiter).split(line);
			final Pizzashack pizzashack = new Pizzashack.Builder().pizzashackId(Iterables.get(values, 0)).pizzaName(Iterables.get(values, 1))
					.description(Iterables.get(values, 2)).price(new BigDecimal(Iterables.get(values, 3))).icon(Iterables.get(values, 4))
					.amount(Integer.valueOf(Iterables.get(values, 5)))
					.createTime(FORMAT.parse(Iterables.get(values, 6)))
					.build();
			pizzashackSet.add(pizzashack);
		}
		return pizzashackSet;
	}

	public static Set<Role> initRoles() throws Exception {
		Set<Role> roleSet = Sets.<Role> newHashSet();
		List<String> lines = readClasspathFile(TEST_ROLE_DATA_FILE);
		for (final String line : lines) {
			Iterable<String> values = Splitter.on(delimiter).split(line);
			final Role role = new Role.Builder().roleName(Iterables.get(values, 0))
					.createTime(FORMAT.parse(Iterables.get(values, 1)))
					.build();
			roleSet.add(role);
		}

		return roleSet;
	}

	public static Map<User, String> initUsers() throws Exception {
		Map<User, String> initUserMap = Maps.<User, String> newHashMap();
		File initPizzashackFile = new File(Resources.getResource(TEST_USER_DATA_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initPizzashackFile, Charsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final User user = new User.Builder()
						.userName(Iterables.get(values, 0))
						.password(Iterables.get(values, 1))
						.createTime(FORMAT.parse(Iterables.get(values, 2)))
						.build();
				userSet.add(user);
			}
		}
		return userSet;
	}

}
