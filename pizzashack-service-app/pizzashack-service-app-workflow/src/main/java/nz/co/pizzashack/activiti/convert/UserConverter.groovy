package nz.co.pizzashack.activiti.convert;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import nz.co.pizzashack.OperationType
import nz.co.pizzashack.activiti.convert.component.PageMapToModel
import nz.co.pizzashack.model.Page
import nz.co.pizzashack.model.workflow.User

import com.google.common.base.Function
import com.google.inject.Inject

class UserConverter {

	@Inject
	JsonSlurper jsonSlurper

	@Inject
	JsonBuilder jsonBuilder

	@Inject
	Function<Map<String, String>, User> userMapToModelConverter

	String userModelToJson(final User user,final OperationType operationType){
		jsonBuilder{
			if(operationType == OperationType.CREATION){
				id user.id
			}
			firstName user.firstName
			lastName user.lastName
			email user.email
			password user.password
		}
		return jsonBuilder.toString()
	}

	User jsonToUserModel(final String jsonText){
		return userMapToModelConverter.apply((Map)jsonSlurper.parseText(jsonText))
	}

	Set<User> jsonToUserModels(final String jsonText){
		Set<User> users = []
		Map resultMap =(Map)jsonSlurper.parseText(jsonText)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				users << userMapToModelConverter.apply((Map)it)
			}
		}
		return users
	}

	Page<User> jsonToUserPage(final String jsonText){
		Map resultMap = (Map)jsonSlurper.parseText(jsonText)
		Page<User> page = new PageMapToModel<User>().apply(resultMap)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				page.content << userMapToModelConverter.apply((Map)it)
			}
		}
		return page
	}
}
