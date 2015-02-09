package nz.co.pizzashack.integration;

import nz.co.pizzashack.integration.config.CamelContextConfiguration;

import org.apache.camel.CamelContext;
import org.apache.camel.guice.CamelModuleWithMatchingRoutes;
import org.apache.camel.impl.ExplicitCamelContextNameStrategy;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spi.CamelContextNameStrategy;
import org.apache.camel.spi.Registry;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;

public class IntegrationModule extends CamelModuleWithMatchingRoutes {
	@Override
	protected void configureCamelContext() {
		bind(CamelContext.class).to(CamelContextConfiguration.class).asEagerSingleton();
		bind(Registry.class).toProvider(RegistryProvider.class);
	}

	public static class RegistryProvider implements Provider<Registry> {
		@Inject
		AmazonS3 amazonS3;

		@Inject
		AmazonSQS amazonSQS;

		@Inject
		AmazonSNSClient amazonSNSClient;

		@Override
		public Registry get() {
			final SimpleRegistry simpleRegistry = new SimpleRegistry();
			simpleRegistry.put("amazonS3", amazonS3);
			simpleRegistry.put("amazonSqs", amazonSQS);
			simpleRegistry.put("amazonSns", amazonSNSClient);
			return simpleRegistry;
		}
	}

	@Provides
	public CamelContextNameStrategy camelContextNameStrategy() {
		return new ExplicitCamelContextNameStrategy("Pizzashack");
	}
}
