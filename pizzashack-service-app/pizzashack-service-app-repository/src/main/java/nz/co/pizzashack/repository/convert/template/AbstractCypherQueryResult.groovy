package nz.co.pizzashack.repository.convert.template

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class AbstractCypherQueryResult {
	//<field,set of values>
	Map<String,Set<String>> dataColumnMap = [:]
	//<column,<uri,<field,value>>>
	Map<String,Map<String,Map<String,String>>> nodeColumnMap = [:]
	Set<AbstractCypherQueryNode> distinctNodes = []
}
