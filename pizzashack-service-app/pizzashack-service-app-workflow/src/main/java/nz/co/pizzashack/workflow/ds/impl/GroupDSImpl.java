package nz.co.pizzashack.workflow.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.OperationType;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Group;
import nz.co.pizzashack.model.workflow.MemberShip;
import nz.co.pizzashack.model.workflow.query.GroupQueryParameter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.workflow.convert.GroupConverter;
import nz.co.pizzashack.workflow.ds.GroupDS;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class GroupDSImpl implements GroupDS {

	@Inject
	@Named("activitiGeneralJsonRestClientAccessor")
	private GeneralJsonRestClientAccessor activitiGeneralJsonRestClientAccessor;

	private final static String GROUP_PATH = "/identity/groups/";

	@Inject
	private GroupConverter groupConverter;

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDSImpl.class);

	@Override
	public Group createGroup(final Group group) throws Exception {
		checkArgument(group != null, "group id can not be null.");
		checkArgument(!StringUtils.isEmpty(group.getId()), "group id can not be null.");
		final String groupCreateJson = groupConverter.groupModelToJson(group, OperationType.CREATION);
		return groupConverter.jsonToGroupModel(activitiGeneralJsonRestClientAccessor.create(GROUP_PATH, groupCreateJson));
	}

	@Override
	public Group getGroupById(final String groupId) throws Exception {
		checkArgument(!StringUtils.isEmpty(groupId), "groupId can not be null.");
		return groupConverter.jsonToGroupModel(activitiGeneralJsonRestClientAccessor.get(GROUP_PATH + groupId));
	}

	@Override
	public Group getGroupByUserId(final String userId) throws Exception {
		final String respString = activitiGeneralJsonRestClientAccessor.query(GROUP_PATH, Maps.newHashMap(new ImmutableMap.Builder<GroupQueryParameter, String>()
				.put(GroupQueryParameter.member, userId)
				.build()));
		LOGGER.info("respString:{} ", respString);
		Set<Group> resultSet = groupConverter.jsonToGroupSet(respString);
		return (Group) resultSet.toArray()[0];
	}

	@Override
	public Page<Group> paginateGroup(final Map<GroupQueryParameter, String> groupQueryParameters, final Integer pageOffset, final Integer pageSize) {
		try {
			return groupConverter.jsonToGroupPage(activitiGeneralJsonRestClientAccessor.paginate(GROUP_PATH, groupQueryParameters, pageOffset, pageSize));
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void deleteGroup(final String groupId) throws Exception {
		checkArgument(!StringUtils.isEmpty(groupId), "groupId can not be null.");
		activitiGeneralJsonRestClientAccessor.delete(GROUP_PATH + groupId);
	}

	@Override
	public MemberShip createMemberToGroup(final String groupId, final String userId) throws Exception {
		checkArgument(!StringUtils.isEmpty(groupId), "groupId can not be null.");
		checkArgument(!StringUtils.isEmpty(userId), "userId can not be null.");
		final String requestBody = "{\"userId\":\"" + userId + "\"}";
		return groupConverter.jsonToMembershipModel(activitiGeneralJsonRestClientAccessor.create(GROUP_PATH + groupId + "/members", requestBody));
	}

	@Override
	public void deleteMemberFromGroup(final String groupId, final String userId) throws Exception {
		checkArgument(!StringUtils.isEmpty(groupId), "groupId can not be null.");
		checkArgument(!StringUtils.isEmpty(userId), "userId can not be null.");
		activitiGeneralJsonRestClientAccessor.delete(GROUP_PATH + groupId + "/members" + userId);
	}

}
