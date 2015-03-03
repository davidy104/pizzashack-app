package nz.co.pizzashack.activiti.ds;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Deployment;
import nz.co.pizzashack.model.workflow.Family;
import nz.co.pizzashack.model.workflow.Identity;
import nz.co.pizzashack.model.workflow.ProcessDefinition;
import nz.co.pizzashack.model.workflow.query.ProcessDefinitionQueryParameter;

public interface ProcessDefinitionActivitiDS {

	Page<Deployment> paginateProcessDefinition(Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters, Integer pageOffset, Integer pageSize);

	ProcessDefinition getProcessDefinitionByProcessDefinitionId(String processDefinitionId) throws Exception;

	ProcessDefinition updateCategory(String processDefinitionId, String category) throws Exception;

	ProcessDefinition suspendProcessDefinition(String processDefinitionId, boolean includeProcessInstances, Date effectiveDate) throws Exception;

	ProcessDefinition activeProcessDefinition(String processDefinitionId, boolean includeProcessInstances, Date effectiveDate) throws Exception;

	Set<Identity> getAllIdentities(String processDefinitionId) throws Exception;

	Identity addIdentity(String processDefinitionId, Family family, String name) throws Exception;

	/**
	 * 
	 * @param processDefinitionId
	 * @param family
	 *            Either users or groups, depending on the type of identity
	 *            link.
	 * @param identityId
	 *            Either the userId or groupId of the identity to remove as
	 *            candidate starter
	 * @return
	 * @throws Exception
	 */
	void deleteIdentity(String processDefinitionId, Family family, String identityId) throws Exception;

	Identity getIdentity(String processDefinitionId, Family family, String identityId) throws Exception;
}
