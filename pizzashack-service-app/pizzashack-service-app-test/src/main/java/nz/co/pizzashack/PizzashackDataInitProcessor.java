package nz.co.pizzashack;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.PizzashackCommentType;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.camel.CamelContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataInitProcessor {

	private static Injector injector;

	private static String LOCAL_IMAGE_FOLDER = "/images/";

	private static final String delimiter = "||";

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private final static String PIZZASHACK_INIT_FILE = "pizzashack-init.txt";
	private final static String USER_INIT_FILE = "user-init.txt";
	private final static String PIZZASHACK_COMMENTS_FILE = "comments-init.txt";
	private final static String VIEWED_FILE = "viewed-init.txt";

	private static Set<Pizzashack> initPizzashacks = Sets.<Pizzashack> newHashSet();
	private static Set<User> initUsers = Sets.<User> newHashSet();
	private static Set<PizzashackComment> initPizzashackComments = Sets.<PizzashackComment> newHashSet();

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackDataInitProcessor.class);

	public static void main(String[] args) throws Exception {
		CamelContext camelContext = null;
		try {
			injector = Guice.createInjector(new ConfigurationServiceModule(), new SharedModule(), new RepositoryModule(),
					new IntegrationModule(), new ServiceModule());
			camelContext = injector.getInstance(CamelContext.class);
			camelContext.start();
			doInit();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (camelContext != null) {
				camelContext.stop();
			}
		}
	}

	private static void doInit() throws Exception {
		initPizzashacks();
		initUsers();
		initComments();
		initViewed();
	}

	public static void initPizzashacks() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(PIZZASHACK_INIT_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				InputStream imageStream = null;
				Pizzashack pizzashack = new Pizzashack.Builder().pizzashackId(Iterables.get(values, 0)).pizzaName(Iterables.get(values, 1))
						.description(Iterables.get(values, 2)).price(new BigDecimal(Iterables.get(values, 3))).icon(Iterables.get(values, 4))
						.amount(Integer.valueOf(Iterables.get(values, 5)))
						.createTime(FORMAT.parse(Iterables.get(values, 6)))
						.build();
				System.out.println("pizzashack:{} " + pizzashack);
				if (!StringUtils.isEmpty(pizzashack.getIcon())) {
					imageStream = PizzashackDataInitProcessor.class.getResourceAsStream(LOCAL_IMAGE_FOLDER + pizzashack.getIcon());
					LOGGER.info("imageStream size:{} ", imageStream.available());
				}
				initPizzashacks.add(pizzashackDS.createPizzashack(pizzashack, pizzashack.getIcon(), imageStream));
			}
		}
	}

	public static void initUsers() throws Exception {
		final UserDS userDs = injector.getInstance(UserDS.class);
		File initFile = new File(Resources.getResource(USER_INIT_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				User user = new User.Builder()
						.userName(Iterables.get(values, 0))
						.password(Iterables.get(values, 1))
						.createTime(FORMAT.parse(Iterables.get(values, 2)))
						.build();
				initUsers.add(userDs.createUser(user));
			}
		}
	}

	private static Pizzashack findPizzashackById(final String pizzashackId) {
		if (!initPizzashacks.isEmpty()) {
			for (final Pizzashack initPizzashack : initPizzashacks) {
				if (initPizzashack.getPizzashackId().equalsIgnoreCase(pizzashackId)) {
					return initPizzashack;
				}
			}
		}
		return null;
	}

	private static User findUserByName(final String userName) {
		if (!initUsers.isEmpty()) {
			for (final User user : initUsers) {
				if (user.getUserName().equalsIgnoreCase(userName)) {
					return user;
				}
			}
		}
		return null;
	}

	public static void initComments() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(PIZZASHACK_COMMENTS_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				// PIZZA-fea77b62-2e9c-43b5-9345-6b5922e37412||john||LIKE||good
				// taste!||2014-10-10
				// 12:23:12
				PizzashackComment comment = new PizzashackComment.Builder()
						.commentType(PizzashackCommentType.getPizzashackCommentType(Iterables.get(values, 2)))
						.message(Iterables.get(values, 3))
						.createTime(FORMAT.parse(Iterables.get(values, 4)))
						.build();

				Pizzashack pizzashack = findPizzashackById(Iterables.get(values, 0));
				User user = findUserByName(Iterables.get(values, 1));

				final String commentUri = pizzashackDS.createPizzashackComment(Iterables.get(values, 0), Iterables.get(values, 1), comment);
				comment.setNodeUri(commentUri);
				comment.setPizzashack(pizzashack);
				comment.setUser(user);
				initPizzashackComments.add(comment);
			}
		}
	}

	public static void initViewed() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(VIEWED_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String pizzashackId = Iterables.get(values, 0);
				pizzashackDS.createViewed(pizzashackId, Iterables.get(values, 1));
				initPizzashacks.add(pizzashackDS.getPizzashackById(pizzashackId));
			}
		}
	}

}
