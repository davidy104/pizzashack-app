package nz.co.pizzashack.repository.test;

import static org.junit.Assert.*
import groovy.json.JsonBuilder
import nz.co.pizzashack.model.Pizzashack

import org.junit.Test

import com.google.common.base.Joiner

class JsonConvertTest {

	@Test
	public void testObjectToJson(){
		Pizzashack model = new Pizzashack()
		model.pizzaName = 'abc'
		model.description = 'pizzashack abc'
		model.price = new BigDecimal("45.03")
		println "$model"
		def builder = new JsonBuilder(model)
		println "${builder.toPrettyString()}"



		String[] fieldValuePairArray = [
			"pizzashackId:${model.pizzashackId}",
			"description:${model.description}",
			"pizzaName:${model.pizzaName}",
			"price:${model.price}",
			"icon:${model.icon}"
		]
		def str = Joiner.on(",").skipNulls().join(fieldValuePairArray)
		println "${str}"
	}
	
	@Test
	public void test1(){
		def jsonBuilder = new JsonBuilder()
		jsonBuilder{
			value "123"
			key "wwewe"
			uri "http://dfedd/"
		}
		println "${jsonBuilder.toPrettyString()}"
	}

	@Test
	public void test() {
		String[] statementArray = [
			"CREATE ( bike:Bike { weight: 10 } )",
			"CREATE ( frontWheel:Wheel { spokes: 3 }"
		]

		def builder = new JsonBuilder()
		builder{
			statements(
					statementArray.collect { it }
					)
			resultDataContents(
					['REST'].collect { it }
					)
		}

		println "${builder.toPrettyString()}"

		Map map = [isbn:"2837283AA", name:"cheese"]

		//		builder(
		//				map.each {k,v->
		//					builder "$k": "$v"
		//				}
		//				)
		//
		//		println "${builder.toPrettyString()}"
		//		def paramStr= builder.toString()

		def stat = "MATCH (book:Book{isbn:{isbn}})"

		builder{
			query stat
			params (
					map.each {k,v->
						builder "$k": "$v"
					}
					)
		}

		println "${builder.toPrettyString()}"
	}
}
