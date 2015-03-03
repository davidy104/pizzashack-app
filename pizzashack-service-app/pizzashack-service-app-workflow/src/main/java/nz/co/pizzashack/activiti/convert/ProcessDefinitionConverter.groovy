package nz.co.pizzashack.activiti.convert

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.text.DateFormat
import java.text.SimpleDateFormat

import nz.co.pizzashack.activiti.convert.component.PageMapToModel
import nz.co.pizzashack.activiti.ds.ProcessDefinitionActionType
import nz.co.pizzashack.model.Page
import nz.co.pizzashack.model.workflow.ProcessDefinition

import com.google.common.base.Function
import com.google.inject.Inject

class ProcessDefinitionConverter {
	@Inject
	JsonSlurper jsonSlurper

	@Inject
	JsonBuilder jsonBuilder

	@Inject
	Function<Map<String, String>, ProcessDefinition> processDefinitionMapToModelConverter

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

	String toProcessDefinitionActionJson(final ProcessDefinitionActionType processDefinitionActionType,final boolean includeProcessInstances, final Date effectiveDate){
		jsonBuilder{
			action processDefinitionActionType.name
			includeProcessInstances includeProcessInstances
			date dateFormat.format(effectiveDate)
		}
		return jsonBuilder.toString()
	}


	ProcessDefinition jsonToProcessDefinitionModel(final String jsonText){
		return processDefinitionMapToModelConverter.apply((Map)jsonSlurper.parseText(jsonText))
	}

	Set<ProcessDefinition> jsonToProcessDefinitionModels(final String jsonText){
		Set<ProcessDefinition> processDefinitions = []
		Map resultMap =(Map)jsonSlurper.parseText(jsonText)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				processDefinitions << processDefinitionMapToModelConverter.apply((Map)it)
			}
		}
		return processDefinitions
	}

	Page<ProcessDefinition> jsonToProcessDefinitionPage(final String jsonText){
		Map resultMap = (Map)jsonSlurper.parseText(jsonText)
		Page<ProcessDefinition> page = new PageMapToModel<ProcessDefinition>().apply(resultMap)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				page.content << processDefinitionMapToModelConverter.apply((Map)it)
			}
		}
		return page
	}
}
