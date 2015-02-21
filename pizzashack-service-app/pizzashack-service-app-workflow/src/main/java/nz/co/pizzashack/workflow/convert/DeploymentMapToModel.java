package nz.co.pizzashack.workflow.convert;

import java.util.Map;

import nz.co.pizzashack.model.workflow.Deployment;

import com.google.common.base.Function;

public class DeploymentMapToModel implements Function<Map<String,String>, Deployment> {

	@Override
	public Deployment apply(final Map<String, String> inputMap) {
		return null;
	}

}
