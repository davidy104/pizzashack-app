package nz.co.pizzashack;

import static nz.co.pizzashack.PizzashackInitUtils.initPizzashackFromFile;

import java.io.BufferedReader;
import java.io.File;
import java.util.Set;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.ds.PizzashackDS;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.model.Pizzashack;
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
	private final static String PIZZASHACK_INIT_FILE = "pizzashack-init.txt";
	private final static String USER_INIT_FILE = "user-init.txt";
	private final static String PIZZASHACK_COMMENTS_FILE = "comments-init.txt";
	private final static String VIEWED_FILE = "viewed-init.txt";
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
	
	public static void doCleanup()throws Exception {
		cleanPizzashacks();
	}
	
	public static void cleanViewed()throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		File initFile = new File(Resources.getResource(VIEWED_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initFile, Charsets.UTF_8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final String pizzashackId = Iterables.get(values, 0);
				
			}
		}
	}

	public static void cleanPizzashacks() throws Exception {
		final PizzashackDS pizzashackDS = injector.getInstance(PizzashackDS.class);
		Set<Pizzashack> initPizzashacks = initPizzashackFromFile();
		for (final Pizzashack pizzashack : initPizzashacks) {
			pizzashackDS.deleteById(pizzashack.getPizzashackId());
		}
	}
}
