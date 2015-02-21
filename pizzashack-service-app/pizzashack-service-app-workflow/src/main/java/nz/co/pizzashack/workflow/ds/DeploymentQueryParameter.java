package nz.co.pizzashack.workflow.ds;

import nz.co.pizzashack.AbstractEnumQueryParameter;

public enum DeploymentQueryParameter implements AbstractEnumQueryParameter{
	name,nameLike,category,categoryNotEquals,tenantId,tenantIdLike,withoutTenantId,sort
}
