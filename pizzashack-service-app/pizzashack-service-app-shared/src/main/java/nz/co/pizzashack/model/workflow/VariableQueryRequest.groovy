package nz.co.pizzashack.model.workflow

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class VariableQueryRequest implements Serializable{

	String name
	String value

	/**
	 * equals, notEquals, equalsIgnoreCase, notEqualsIgnoreCase, lessThan, greaterThan, lessThanOrEquals, greaterThanOrEquals and like
	 */
	String operator

	/**
	 * string,short,integer,long,double,boolean,date
	 */
	String type
}
