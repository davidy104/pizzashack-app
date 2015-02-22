package nz.co.pizzashack.workflow.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.workflow.DeploymentResource;

import com.google.common.base.Function;

public class DeploymentResourceMapToModel implements Function<Map<String,String>, DeploymentResource> {

	@Override
	public DeploymentResource apply(Map<String, String> inputMap) {
		DeploymentResource result = null;
		if(inputMap != null){
			result = new DeploymentResource.Builder()
			.dataUrl(inputMap.get("dataUrl"))
			.id(inputMap.get("id"))
			.mediaType(inputMap.get("mediaType"))
			.type(inputMap.get("type"))
			.build();
		}
		return result;
	}

}
