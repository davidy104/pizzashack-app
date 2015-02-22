package nz.co.pizzashack.model.workflow.query;

import nz.co.pizzashack.AbstractEnumQueryParameter;

public enum DeploymentQueryParameter implements AbstractEnumQueryParameter{
	name,nameLike,category,categoryNotEquals,tenantId,tenantIdLike,withoutTenantId,sort
}
