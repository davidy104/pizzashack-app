package nz.co.pizzashack;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.config.ResourceModule;
import nz.co.pizzashack.integration.IntegrationModule;
import nz.co.pizzashack.repository.RepositoryModule;

import org.apache.camel.CamelContext;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jolokia.jvmagent.JvmAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.jdi.Bootstrap;
import com.sun.jersey.api.client.Client;

/**
 * 
 * @author Davidy
 *
 */
public class BootStrap extends GuiceResteasyBootstrapServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

	private CamelContext camelContext;
	private Client jerseyClient;

	@Override
	protected void withInjector(Injector injector) {
		try {
			this.camelContext = injector.getInstance(CamelContext.class);
			this.camelContext.start();
		} catch (final Exception e) {
			LOGGER.error("Failed to start camel context", e);
		}
		jerseyClient = injector.getInstance(Client.class);
	}

	@Override
	protected List<? extends Module> getModules(final ServletContext context) {
		JvmAgent.agentmain(null);
		return Arrays.asList(new ConfigurationServiceModule(),
				new SharedModule(),
				new IntegrationModule(),
				new RepositoryModule(),
				new ServiceModule(),
				new ResourceModule());
	}

	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		if (this.camelContext != null) {
			try {
				this.camelContext.stop();
			} catch (final Exception e) {
				LOGGER.error("Failed to stop camel context", e);
			}
		}
		jerseyClient.destroy();
		super.contextDestroyed(event);
		JvmAgent.agentmain("mode=stop");
	}

}
