package nz.co.pizzashack.workflow.convert;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.util.Set

import nz.co.pizzashack.OperationType
import nz.co.pizzashack.model.Page
import nz.co.pizzashack.model.workflow.User
import nz.co.pizzashack.workflow.convert.component.PageMapToModel
import nz.co.pizzashack.workflow.convert.component.UserMapToModel

import com.google.inject.Inject

class UserConverter {

	@Inject
	JsonSlurper jsonSlurper

	@Inject
	JsonBuilder jsonBuilder

	@Inject
	UserMapToModel userMapToModel

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

	User jsonToUser(final String jsonText){
		return userMapToModel.apply((Map)jsonSlurper.parseText(jsonText))
	}

	Set<User> jsonToUsers(final String jsonText){
		Set<User> users = []
		Map resultMap =(Map)jsonSlurper.parseText(jsonText)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				users << userMapToModel.apply((Map)it)
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
				page.content << userMapToModel.apply((Map)it)
			}
		}
		return page
	}
}
