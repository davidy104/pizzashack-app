package nz.co.pizzashack.activiti.ds;

import java.util.Map;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Group;
import nz.co.pizzashack.model.workflow.MemberShip;
import nz.co.pizzashack.model.workflow.query.GroupQueryParameter;

public interface GroupActivitiDS {
	Group createGroup(Group group) throws Exception;

	Group getGroupById(String groupId) throws NotFoundException;

	Group getGroupByUserId(String userId) throws Exception;

	Page<Group> paginateGroup(Map<GroupQueryParameter, String> groupQueryParameters, Integer pageOffset, Integer pageSize);

	void deleteGroup(String groupId) throws Exception;

	MemberShip createMemberToGroup(String groupId, String userId) throws Exception;

	void deleteMemberFromGroup(String groupId, String userId) throws Exception;
}
