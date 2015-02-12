package nz.co.pizzashack.integration.route;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImageToS3Route extends RouteBuilder {

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	@SuppressWarnings("unchecked")
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

		from("direct:ImageToS3")
				.routeId("ImageProcess")
				.setExchangePattern(ExchangePattern.OutOnly)
				.threads()
				.executorServiceRef("genericThreadPool")
				.to("direct:pushImage")
				.end()
				.end();

		from("direct:pushImage")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						final InputStream imageStream = exchange.getIn().getBody(InputStream.class);
						final int available = imageStream.available();
						exchange.getIn().setHeader("CamelAwsS3ContentLength", available);
					}
				})
				.setHeader("CamelAwsS3ContentType", constant("image/png"))
				.setHeader("CamelAwsS3Key", simple("${property.outputPath}"))
				.to("aws-s3://" + awsS3Bucket + "?amazonS3Client=#amazonS3");
	}
}
