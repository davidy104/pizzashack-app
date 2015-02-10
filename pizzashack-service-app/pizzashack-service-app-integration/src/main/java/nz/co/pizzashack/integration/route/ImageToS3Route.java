package nz.co.pizzashack.integration.route;

import nz.co.pizzashack.config.ConfigurationService;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImageToS3Route extends RouteBuilder {

	@Inject
	private ConfigurationService configurationService;

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	@Override
	public void configure() throws Exception {

		onException(java.net.SocketTimeoutException.class,
				java.net.ConnectException.class, com.amazonaws.services.s3.model.AmazonS3Exception.class)
				.maximumRedeliveries(2)
				.redeliveryDelay(1000)
				.handled(true)
				.to("log:errors?level=ERROR&showAll=true&multiline=true");

		onException(Exception.class)
				.handled(true)
				.to("log:errors?level=ERROR&showAll=true&multiline=true");

		from("direct:ImageProcess")
				.routeId("ImageProcess")
				.setExchangePattern(ExchangePattern.OutOnly)
				.setProperty("s3Path", simple("${body.s3Path}"))
				.setProperty("tag", simple("${body.tag}"))
				.threads()
				.executorServiceRef("genericThreadPool")
				.to("direct:pushImage")
				.end()
				.end();

		from("direct:pushImage")
//				.process(imageScalingProcessor)
				.setHeader("CamelAwsS3ContentType", constant("image/jpeg"))
				.setHeader("CamelAwsS3ContentLength", simple("${property.imgStreamAvailable}"))
				.setHeader("CamelAwsS3Key", simple("${property.outputPath}"))
				.to("aws-s3://"+awsS3Bucket+"?amazonS3Client=#amazonS3");
	}
}
