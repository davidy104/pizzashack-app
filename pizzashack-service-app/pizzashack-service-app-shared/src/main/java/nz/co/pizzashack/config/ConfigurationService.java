package nz.co.pizzashack.config;

import com.amazonaws.auth.AWSCredentials;

public interface ConfigurationService {
	AWSCredentials getAWSCredentials();

	String getNeo4jRestHostUri();

	String getAwsS3BucketName();
	
	ActivitiRestConfig getActivitiRestConfig();
}
