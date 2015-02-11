package nz.co.pizzashack.ds.impl;

import static nz.co.pizzashack.util.GenericUtils.FOLDER_SUFFIX;
import static nz.co.pizzashack.util.GenericUtils.formatPath;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import nz.co.pizzashack.ds.AwsS3GeneralService;
import nz.co.pizzashack.model.S3Asset;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AwsS3GeneralServiceImpl implements AwsS3GeneralService {

	@Inject
	@Named("AWS.S3_BUCKET_NAME")
	private String awsS3Bucket;

	@Inject
	private AmazonS3 amazonS3;

	@Override
	public void putAsset(final String key, final InputStream asset, final String contentType) {
		try {
			if (asset != null) {
				ObjectMetadata meta = new ObjectMetadata();
				meta.setContentLength(asset.available());
				if (!StringUtils.isEmpty(contentType)) {
					meta.setContentType(contentType);
				}
				amazonS3.putObject(new PutObjectRequest(awsS3Bucket, key, asset, meta));
			}
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public List<String> getAssetList(final String prefix) {
		List<String> resultList = Collections.<String> emptyList();
		ObjectListing objList = amazonS3.listObjects(awsS3Bucket, formatPath(prefix));
		if (objList != null) {
			for (final S3ObjectSummary summary : objList.getObjectSummaries()) {
				// ignore folders
				if (!summary.getKey().endsWith(FOLDER_SUFFIX)) {
					resultList.add(summary.getKey().substring(prefix.length()));
				}
			}
		}
		return resultList;
	}

	@Override
	public S3Asset getAssetByName(final String name) {
		S3Object obj = amazonS3.getObject(new GetObjectRequest(awsS3Bucket, name));
		if (obj != null) {
			return new S3Asset.Builder().bucketName(obj.getBucketName())
					.size(obj.getObjectMetadata().getContentLength())
					.content(obj.getObjectContent()).key(obj.getKey()).build();
		}
		return null;
	}

	@Override
	public void deleteAssert(final String key) {
		amazonS3.deleteObject(awsS3Bucket, key);
	}

}
