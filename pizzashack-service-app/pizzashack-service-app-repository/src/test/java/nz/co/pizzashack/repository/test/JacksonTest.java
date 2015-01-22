package nz.co.pizzashack.repository.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {

	@SuppressWarnings({ "unchecked" })
	@Test
	public void test() throws Exception {
		final String json = "{\"commit\" : \"http://localhost:7474/db/data/transaction/1/commit\",\"columns\" : [ \"n\",\"m\" ]}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

		List<String> columnList = (List)jsonMap.get("columns");
		for (Object column : columnList) {
			System.out.println("column:{} " + column);
		}
	}

}
