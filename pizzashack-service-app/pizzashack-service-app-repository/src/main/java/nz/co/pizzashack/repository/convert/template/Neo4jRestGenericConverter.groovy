package nz.co.pizzashack.repository.convert.template;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class Neo4jRestGenericConverter {

	JsonBuilder jsonBuilder

	JsonSlurper jsonSlurper

	public Neo4jRestGenericConverter(final JsonBuilder jsonBuilder,final JsonSlurper jsonSlurper) {
		this.jsonBuilder = jsonBuilder;
		this.jsonSlurper = jsonSlurper;
	}
	/**
	 * {
	 "query" : "CREATE (n:Person { name : {name} }) RETURN n",
	 "params" : {
	 "name" : "Andres"
	 }
	 }
	 * @param cypherQueryStatement
	 * @param queryParams
	 * @return
	 */
	String cypherQueryRequestConvert(final String cypherQueryStatement,final Map<String,String> queryParams){
		jsonBuilder{
			query "${cypherQueryStatement}"
			params "${key}"
		}
		return jsonBuilder.toString()
	}

	/**
	 * 
	 * @param statementArray
	 * @return
	 */
	String transCreateStatementsConvert(String[] statementArray){
		jsonBuilder{
			statements(
					statementArray.collect {it}
					)
			resultDataContents(
					['REST'].collect {it}
					)
		}
		return jsonBuilder.toString()
	}

	String buildUniqueNodeRequest(final String existedNodeUri, final String key, final String value){
		jsonBuilder{
			value "${value}"
			key "${key}"
			uri "${existedNodeUri}"
		}
		return jsonBuilder.toString()
	}

	/**
	 * 
	 * @param jsonMap
	 * @return
	 */
	Map<String,String> transCypherRestFormatResponseConvert(final Map<String, Object> jsonMap){
		Map<String,String> convertedMap = [:]
		List resultsList = (List)jsonMap['results']
		if(resultsList){
			Map resultMap = resultsList[0]
			List columnList = (List)resultMap['columns']
			List dataList = (List)resultMap['data']
			columnList.eachWithIndex {obj, i ->
				Map dataMap = (Map)dataList[i]
				Map metaMap = ((List)dataMap['rest'])[0]
				convertedMap.put(obj, metaMap['self'])
			}
		}
		return convertedMap;
	}

	/**
	 * 
	 * @param jsonInput
	 * @param distinctColumn
	 * @return
	 * @throws Exception
	 */
	AbstractCypherQueryResult cypherQueryRespConvert(final String jsonInput,final String distinctColumn) {
		boolean distinctNode = false
		AbstractCypherQueryResult result = new AbstractCypherQueryResult()
		Map metaMap = (Map)jsonSlurper.parseText(jsonInput)
		List columnList = metaMap.get('columns')
		List metaDataList = metaMap.get('data')
		if(metaDataList){
			if(distinctColumn){
				metaDataList.each {meta->
					boolean found = false
					meta.eachWithIndex {obj,i->
						String column = columnList.get(i)
						if(!found){
							if(column == distinctColumn && obj instanceof Map){
								distinctNode = true
								Map<String,Object> tdistinctMap = (Map)obj
								Map<String,String> distinctDataMap = (Map)tdistinctMap.get("data")
								String nodeUri = tdistinctMap.get("self")
								result.distinctNodes << new AbstractCypherQueryNode(uri:nodeUri,column:column,dataMap:distinctDataMap)
							}
						}
					}
				}
			}
			if(distinctNode){
				metaDataList.each {meta->
					boolean found = false
					AbstractCypherQueryNode disctNode
					Map<String,AbstractCypherQueryNode> relatedNodeMap = [:]
					Map<String,String> dataValuesMap = [:]

					meta.eachWithIndex {obj,i->
						String column = columnList.get(i)
						if(!found && distinctColumn == column){
							String curNodeUri = ((Map)obj).get("self")
							disctNode = result.distinctNodes.find{
								it.uri == curNodeUri && it.column == column
							}
						}
						if(distinctColumn != column){
							if(obj instanceof Map){
								Map<String,Object> nodeMap = (Map)obj
								String nodeUri = (String)(nodeMap.get("self"))
								Map<String,String> nodeDataMap = nodeMap.get("data")
								def queryNode = new AbstractCypherQueryNode(uri:nodeUri,column:column,dataMap:nodeDataMap)
								relatedNodeMap.put(column, queryNode)
							}else{
								dataValuesMap.put(column, (String)obj)
							}
						}
					}
					relatedNodeMap.each {k,v->
						if(disctNode.relationAbstractCypherQueryNodes.containsKey(k)){
							disctNode.relationAbstractCypherQueryNodes.get(k) << v
						}else{
							disctNode.relationAbstractCypherQueryNodes.put(k, [v] as Set)
						}
					}

					dataValuesMap.each{k,v->
						if(disctNode.relationDataMap.containsKey(k)){
							disctNode.relationDataMap.get(k) << v
						}else{
							disctNode.relationDataMap.put(k, [v] as Set)
						}
					}
				}
			} else {
				metaDataList.each {meta->
					meta.eachWithIndex {obj,i->
						String column = columnList.get(i)
						if(obj instanceof Map){
							Map<String,Object> nodeMap = (Map)obj
							String nodeUri = (String)(nodeMap.get("self"))
							Map<String,String> nodeDataMap = nodeMap.get("data")

							Map<String,Map<String,String>> internalMap = [:]
							if(result.nodeColumnMap.containsKey(column)){
								internalMap = result.nodeColumnMap.get(column)
							}
							if(!internalMap.containsKey(nodeUri)){
								internalMap.put(nodeUri, nodeDataMap)
							}
							result.nodeColumnMap.put(column, internalMap)
						} else {
							Set<String> dataSet = []
							if(result.dataColumnMap.containsKey(column)){
								dataSet = result.dataColumnMap.get(column)
							}
							dataSet << (String)obj
							result.dataColumnMap.put(column, dataSet)
						}
					}
				}
			}
		}
		return result
	}
}
