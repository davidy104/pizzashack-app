package nz.co.pizzashack.activiti.ds.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.activiti.convert.ProcessDefinitionConverter;
import nz.co.pizzashack.activiti.ds.ProcessDefinitionActionType;
import nz.co.pizzashack.activiti.ds.ProcessDefinitionActivitiDS;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Deployment;
import nz.co.pizzashack.model.workflow.Family;
import nz.co.pizzashack.model.workflow.Identity;
import nz.co.pizzashack.model.workflow.ProcessDefinition;
import nz.co.pizzashack.model.workflow.query.ProcessDefinitionQueryParameter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ProcessDefinitionActivitiDSImpl implements ProcessDefinitionActivitiDS {
	@Inject
	@Named("activitiGeneralJsonRestClientAccessor")
	private GeneralJsonRestClientAccessor activitiGeneralJsonRestClientAccessor;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionActivitiDSImpl.class);

	private static final String PROCESSDEFINITION_BASE_URL = "/repository/process-definitions/";

	@Inject
	private ProcessDefinitionConverter processDefinitionConverter;

	@Override
	public Page<Deployment> paginateProcessDefinition(Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters, Integer pageOffset, Integer pageSize) {
		return null;
	}

	@Override
	public ProcessDefinition getProcessDefinitionByProcessDefinitionId(String processDefinitionId) throws Exception {
		checkArgument(!StringUtils.isEmpty(processDefinitionId), "processDefinitionId can not be null.");
		try {
			return processDefinitionConverter.jsonToProcessDefinitionModel(activitiGeneralJsonRestClientAccessor.get(PROCESSDEFINITION_BASE_URL + processDefinitionId));
		} catch (final Exception e) {
			if (!(e instanceof NotFoundException)) {
				throw new IllegalStateException(e);
			}
			throw new NotFoundException(e);
		}
	}

	@Override
	public ProcessDefinition updateCategory(String processDefinitionId, String category) throws Exception {
		return null;
	}

	@Override
	public ProcessDefinition actionProcess(ProcessDefinitionActionType actionType, String processDefinitionId, boolean includeProcessInstances, Date effectiveDate)
			throws Exception {
		return null;
	}

	@Override
	public Set<Identity> getAllIdentities(String processDefinitionId) throws Exception {
		return null;
	}

	@Override
	public Identity addIdentity(String processDefinitionId, Family family, String name) throws Exception {
		return null;
	}

	@Override
	public void deleteIdentity(String processDefinitionId, Family family, String identityId) throws Exception {

	}

	@Override
	public Identity getIdentity(String processDefinitionId, Family family, String identityId) throws Exception {
		return null;
	}

}
