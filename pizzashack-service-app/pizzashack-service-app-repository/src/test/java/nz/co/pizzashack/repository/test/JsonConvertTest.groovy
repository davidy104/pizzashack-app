package nz.co.pizzashack.repository.test;

import static org.junit.Assert.*
import groovy.json.JsonBuilder

import org.junit.Test

import com.google.common.base.Joiner;

class JsonConvertTest {
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
		
		String returnedString = Joiner.on(",").withKeyValueSeparator(":").join(map)
		
		

		builder{
			query "MATCH (book:${NodesLabel.Book.name()}{isbn:{isbn}})"
			params builder(map).toString()
		}

		println "${builder.toPrettyString()}"
	}
}
