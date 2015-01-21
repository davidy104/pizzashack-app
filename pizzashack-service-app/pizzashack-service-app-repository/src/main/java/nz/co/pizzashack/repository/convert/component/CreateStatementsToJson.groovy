package nz.co.pizzashack.repository.convert.component

import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j

import com.google.common.base.Function
@Slf4j
class CreateStatementsToJson implements Function<String[], String> {

	JsonBuilder jsonBuilder;

	public CreateStatementsToJson(JsonBuilder jsonBuilder) {
		this.jsonBuilder = jsonBuilder;
	}

	@Override
	public String apply(String[] statementArray) {
		jsonBuilder{
			statements(
					statementArray.collect {it}
					)
			resultDataContents(
					['REST'].collect {it}
					)
		}
		return jsonBuilder.toString();
	}
}
