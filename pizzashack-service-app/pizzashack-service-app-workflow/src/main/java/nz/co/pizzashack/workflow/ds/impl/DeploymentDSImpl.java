package nz.co.pizzashack.workflow.ds.impl;

import java.io.File;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.workflow.Deployment;
import nz.co.pizzashack.model.workflow.DeploymentResource;
import nz.co.pizzashack.model.workflow.query.DeploymentQueryParameter;
import nz.co.pizzashack.util.GeneralJsonRestClientAccessor;
import nz.co.pizzashack.workflow.convert.component.DeploymentMapToModel;
import nz.co.pizzashack.workflow.convert.component.DeploymentResourceMapToModel;
import nz.co.pizzashack.workflow.ds.DeploymentDS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DeploymentDSImpl implements DeploymentDS{

	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentDSImpl.class);

	@Inject
	@Named("activitiGeneralJsonRestClientAccessor")
	private GeneralJsonRestClientAccessor activitiGeneralJsonRestClientAccessor;

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;
	
	private final static String DEPLOYMENT_PATH="/repository/deployments/";
	
	@Inject
	private DeploymentMapToModel deploymentMapToModel;
	
	@Inject
	private DeploymentResourceMapToModel deploymentResourceMapToModel;
	
	@Override
	public Deployment deployment(final String name,final String category,final File uploadFile) throws Exception {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Deployment getDeployment(final String name,final String category) throws Exception {
		final String respString = activitiGeneralJsonRestClientAccessor.query(DEPLOYMENT_PATH, Maps.newHashMap(new ImmutableMap.Builder<DeploymentQueryParameter, String>()
				.put(DeploymentQueryParameter.tenantId, name+":"+category)
				.build()));
		
		return deploymentMapToModel.apply(jacksonObjectMapper.readValue(respString, Map.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Deployment getDeployment(final String deploymentId) throws Exception {
		final String respString = activitiGeneralJsonRestClientAccessor.get(DEPLOYMENT_PATH+deploymentId);
		return deploymentMapToModel.apply(jacksonObjectMapper.readValue(respString, Map.class));
	}

	@Override
	public void unDeployment(final String deploymentId) throws Exception {
		activitiGeneralJsonRestClientAccessor.delete(DEPLOYMENT_PATH+deploymentId);
	}

	@Override
	public Set<DeploymentResource> getDeploymentResource(final String deploymentId) throws Exception {
		activitiGeneralJsonRestClientAccessor.get(DEPLOYMENT_PATH+deploymentId+"/resources");
		return null;
	}

	@Override
	public Page<Deployment> paginateDeployment(final Map<DeploymentQueryParameter, String> deploymentQueryParameters,
			final Integer pageOffset, Integer pageSize) {
		return null;
	}

}
