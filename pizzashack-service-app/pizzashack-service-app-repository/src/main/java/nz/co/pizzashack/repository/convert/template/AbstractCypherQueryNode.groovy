package nz.co.pizzashack.repository.convert.template

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@EqualsAndHashCode(includes=["uri","column"])
@ToString(includeNames = true, includeFields=true)
class AbstractCypherQueryNode {
	String uri
	String column
	//<field,value>
	Map<String,String> dataMap =[:]
	//<column,set of value>
	Map<String,Set<String>> relationDataMap = [:]
	//<column,set of nodes>
	Map<String,Set<AbstractCypherQueryNode>> relationAbstractCypherQueryNodes = [:]
}
