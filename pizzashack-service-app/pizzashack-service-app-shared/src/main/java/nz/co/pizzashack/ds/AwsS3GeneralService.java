package nz.co.pizzashack.ds;

import java.io.InputStream;
import java.util.List;

import nz.co.pizzashack.model.S3Asset;

public interface AwsS3GeneralService {
	void putAsset(String key, InputStream asset, String contentType);

	List<String> getAssetList(String prefix);

	S3Asset getAssetByName(String name);

	void deleteAssert(String key);
}
