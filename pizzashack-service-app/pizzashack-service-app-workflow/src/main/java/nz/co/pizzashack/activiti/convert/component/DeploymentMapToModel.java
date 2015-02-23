package nz.co.pizzashack.activiti.convert.component;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nz.co.pizzashack.model.workflow.Deployment;

import com.google.common.base.Function;
import static nz.co.pizzashack.util.GenericUtils.parseToDate;
public class DeploymentMapToModel implements Function<Map<String,String>, Deployment> {

	@Override
	public Deployment apply(final Map<String, String> inputMap) {
		Deployment deployment = null;
		if(inputMap != null){
			deployment = new Deployment.Builder()
			.category(inputMap.get("category"))
			.id(inputMap.get("id"))
			.name(inputMap.get("name"))
			.tenantId(inputMap.get("tenantId"))
			.url(inputMap.get("url"))
			.build();
			if(!StringUtils.isEmpty(inputMap.get("deploymentTime"))){
				deployment.setDeploymentTime(parseToDate("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", inputMap.get("deploymentTime")));
			}
		}
		return deployment;
	}

}
