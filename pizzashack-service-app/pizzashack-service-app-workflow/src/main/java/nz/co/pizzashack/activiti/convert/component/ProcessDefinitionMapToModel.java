package nz.co.pizzashack.activiti.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.workflow.ProcessDefinition;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class ProcessDefinitionMapToModel implements Function<Map<String, String>, ProcessDefinition> {

	@Override
	public ProcessDefinition apply(Map<String, String> inputMap) {
		ProcessDefinition processDefinition = null;
		if (inputMap != null) {
			processDefinition = new ProcessDefinition.Builder()
					.id(inputMap.get("id"))
					.url(inputMap.get("url"))
					.key(inputMap.get("key"))
					.name(inputMap.get("name"))
					.description(inputMap.get("description"))
					.deploymentId(inputMap.get("deploymentId"))
					.deploymentUrl(inputMap.get("deploymentUrl"))
					.resource(inputMap.get("resource"))
					.diagramResource(inputMap.get("diagramResource"))
					.category(inputMap.get("category"))
					.build();

			if (!StringUtils.isEmpty(inputMap.get("version"))) {
				processDefinition.setVersion(Integer.valueOf(inputMap.get("version")));
			}

			if (!StringUtils.isEmpty(inputMap.get("graphicalNotationDefined"))) {
				processDefinition.setGraphicalNotationDefined(Boolean.valueOf(inputMap.get("graphicalNotationDefined")));
			}
			if (!StringUtils.isEmpty(inputMap.get("suspended"))) {
				processDefinition.setSuspended(Boolean.valueOf(inputMap.get("suspended")));
			}
			if (!StringUtils.isEmpty(inputMap.get("startFormDefined"))) {
				processDefinition.setStartFormDefined(Boolean.valueOf(inputMap.get("startFormDefined")));
			}
		}
		return processDefinition;
	}

}
