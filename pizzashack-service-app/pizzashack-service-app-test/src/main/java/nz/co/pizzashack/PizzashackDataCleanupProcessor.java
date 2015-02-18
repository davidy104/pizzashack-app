package nz.co.pizzashack;

import java.io.BufferedReader;
import java.io.File;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.ds.UserDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.camel.CamelContext;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class PizzashackDataCleanupProcessor {

	private static Injector injector = null;
	private final static String PIZZASHACK_INIT_FILE = "init/pizzashack-init.txt";
	private final static String USER_INIT_FILE = "init/user-init.txt";
	private final static String PIZZASHACK_COMMENTS_FILE = "init/comments-init.txt";
	private final static String VIEWED_FILE = "init/viewed-init.txt";
	private static final String delimiter = "||";

	public static void main(String[] args) throws Exception {
		CamelContext camelContext = null;
		try {
			injector = Guice.createInjector(new ConfigurationServiceModule(), new SharedModule(), new RepositoryModule(),
					new IntegrationModule(), new ServiceModule());
			camelContext = injector.getInstance(CamelContext.class);
			camelContext.start();
			doCleanup();
		} catch (Exception e) {
			throw e;
		} finally {
			camelContext.stop();
		}
	}

	public static void doCleanup() throws Exception {
		cleanComments();
		cleanViewed();
		cleanUsers();
		cleanPizzashacks();
	}

	public static void cleanComments() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(PIZZASHACK_COMMENTS_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String pizzashackId = Iterables.get(values, 0);
				pizzashackDS.deleteCommentByPizzashackId(pizzashackId);
			}
		}
	}

	public static void cleanViewed() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(VIEWED_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String pizzashackId = Iterables.get(values, 0);
				final String userName = Iterables.get(values, 1);
				pizzashackDS.deleteViewed(pizzashackId, userName);
			}
		}
	}

	public static void cleanUsers() throws Exception {
		final UserDS userDS = injector.getInstance(UserDS.class);
		File initFile = new File(Resources.getResource(USER_INIT_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String userName = Iterables.get(values, 0);
				userDS.deleteUserByName(userName);
			}
		}
	}

	public static void cleanPizzashacks() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(PIZZASHACK_INIT_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String pizzashackId = Iterables.get(values, 0);
				pizzashackDS.deleteById(pizzashackId);
			}
		}
	}
}
