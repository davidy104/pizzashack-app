package nz.co.pizzashack.activiti.convert

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import nz.co.pizzashack.OperationType
import nz.co.pizzashack.activiti.convert.component.PageMapToModel;
import nz.co.pizzashack.model.Page
import nz.co.pizzashack.model.workflow.Group
import nz.co.pizzashack.model.workflow.MemberShip

import com.google.common.base.Function
import com.google.inject.Inject
import com.google.inject.name.Named

class GroupConverter {

	@Inject
	JsonSlurper jsonSlurper

	@Inject
	JsonBuilder jsonBuilder

	@Inject
	@Named("membershipMapToModelConverter")
	Function<Map<String, String>, MemberShip> membershipMapToModelConverter

	@Inject
	@Named("groupMapToModelConverter")
	Function<Map<String, String>, Group> groupMapToModelConverter

	MemberShip jsonToMembershipModel(final String jsonText){
		return membershipMapToModelConverter.apply((Map)jsonSlurper.parseText(jsonText))
	}

	Group jsonToGroupModel(final String jsonText){
		return groupMapToModelConverter.apply((Map)jsonSlurper.parseText(jsonText))
	}

	Set<Group> jsonToGroupSet(final String jsonText){
		Set<Group> groups = []
		List resultList = (List)((Map)jsonSlurper.parseText(jsonText))['data']
		if(resultList){
			resultList.each {
				groups << groupMapToModelConverter.apply((Map)it)
			}
		}
		return groups
	}

	Page<Group> jsonToGroupPage(final String jsonText){
		Map resultMap = (Map)jsonSlurper.parseText(jsonText)
		Page page = new PageMapToModel().apply(resultMap)
		List resultList = resultMap['data']
		if(resultList){
			resultList.each {
				page.content << groupMapToModelConverter.apply((Map)it)
			}
		}
		return page
	}

	String groupModelToJson(final Group group,final OperationType operationType){
		jsonBuilder{
			if(operationType == OperationType.CREATION){
				id group.id
			}
			name group.name
			type group.type
		}
		return jsonBuilder.toString()
	}
}
