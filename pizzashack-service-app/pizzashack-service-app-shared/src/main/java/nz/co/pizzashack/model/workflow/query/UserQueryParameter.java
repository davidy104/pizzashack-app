package nz.co.pizzashack.model.workflow.query;

import nz.co.pizzashack.AbstractEnumQueryParameter;

public enum UserQueryParameter implements AbstractEnumQueryParameter {
	id, firstName, lastName, email, firstNameLike, lastNameLike, emailLike, memberOfGroup, potentialStarter, sort
}
