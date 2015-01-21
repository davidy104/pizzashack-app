package nz.co.pizzashack.repository.test;

import static org.junit.Assert.*
import groovy.json.JsonBuilder

import org.junit.Test

class JsonConvertTest {
	@Test
	public void test() {
		String[] statementArray = [
			"CREATE ( bike:Bike { weight: 10 } )",
			"CREATE ( frontWheel:Wheel { spokes: 3 }"
		]



		//		statementArray.each {
		//			println "$it"
		//		}

		def builder = new JsonBuilder()
		builder{
			statements(
					statementArray.collect {it }
					)
			resultDataContents(
					['REST'].collect {it}
					)
		}

		println "${builder.toPrettyString()}"
	}
}
