package nz.co.pizzashack;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import java.util.concurrent.Executors;

import nz.co.pizzashack.config.ConfigurationService;
import nz.co.pizzashack.ds.AwsS3GeneralService;
import nz.co.pizzashack.ds.impl.AwsS3GeneralServiceImpl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class SharedModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(Names.named("integrationEventBus")).toProvider(IntegrationEventBusProvider.class).asEagerSingleton();
		bind(Client.class).toProvider(JerseyClientProvider.class).asEagerSingleton();
		bind(AmazonS3.class).toProvider(AmazonS3Provider.class).asEagerSingleton();
		bind(AmazonSQS.class).toProvider(AmazonSQSProvider.class).asEagerSingleton();
		bind(AmazonSNSClient.class).toProvider(AmazonSNSClientProvider.class).asEagerSingleton();
		bind(AwsS3GeneralService.class).to(AwsS3GeneralServiceImpl.class).asEagerSingleton();
	}

	public static class IntegrationEventBusProvider implements Provider<EventBus> {
		@Override
		public EventBus get() {
			return new AsyncEventBus(Executors.newCachedThreadPool());
		}
	}

	@Provides
	@Singleton
	@Named("jacksonObjectMapper")
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}

	@Provides
	@Singleton
	@Named("jsonSlurper")
	public JsonSlurper jsonSlurper() {
		return new JsonSlurper();
	}

	@Provides
	@Singleton
	@Named("jsonBuilder")
	public JsonBuilder jsonBuilder() {
		return new JsonBuilder();
	}

	public static class JerseyClientProvider implements Provider<Client> {
		@Override
		public Client get() {
			com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
					Boolean.TRUE);
			Client client = Client.create(config);
			client.setConnectTimeout(10000);
			client.setReadTimeout(10000);
			client.addFilter(new LoggingFilter(System.out));
			return client;
		}
	}

	public static class AmazonSQSProvider implements Provider<AmazonSQS> {
		@Inject
		ConfigurationService configurationService;

		@Override
		public AmazonSQS get() {
			final AWSCredentials awsCredentials = configurationService
					.getAWSCredentials();
			if (awsCredentials != null) {
				AmazonSQS amazonSqs = new AmazonSQSClient(awsCredentials);
				amazonSqs.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
				return amazonSqs;
			}
			return null;
		}
	}

	public static class AmazonSNSClientProvider implements
			Provider<AmazonSNSClient> {
		@Inject
		ConfigurationService configurationService;

		@Override
		public AmazonSNSClient get() {
			final AWSCredentials awsCredentials = configurationService
					.getAWSCredentials();
			if (awsCredentials != null) {
				AmazonSNSClient snsClient = new AmazonSNSClient(awsCredentials);
				snsClient.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
				return snsClient;
			}
			return null;
		}
	}

	public static class AmazonS3Provider implements Provider<AmazonS3> {
		@Inject
		ConfigurationService configurationService;

		@Override
		public AmazonS3 get() {
			final ClientConfiguration clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTP);
			final AWSCredentials awsCredentials = configurationService
					.getAWSCredentials();
			if (awsCredentials != null) {
				AmazonS3 amazonS3 = new AmazonS3Client(awsCredentials,
						clientConfig);
				amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_2));
				return amazonS3;
			}
			return null;
		}
	}

}
