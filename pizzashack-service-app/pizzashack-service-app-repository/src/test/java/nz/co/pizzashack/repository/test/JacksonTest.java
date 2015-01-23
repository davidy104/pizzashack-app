package nz.co.pizzashack.repository.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {

	@SuppressWarnings({ "unchecked" })
	@Test
	public void test() throws Exception {
		String str = "MATCH (book:Book{isbn:{isbn}}) OPTIONAL MATCH (:Publisher)<-[publishedBy:PublishedBy]-(book) RETURN DISTINCT book, publishedBy";
		String str1 = "MATCH (book:Book{isbn:{isbn}}) OPTIONAL MATCH (:Publisher)<-[publishedBy:PublishedBy]-(book) RETURN Distinct book, publishedBy";

		int len = "DISTINCT".length();
		
		int disPos = str.toUpperCase().indexOf("DISTINCT");

		if (disPos != -1) {
			String distinctPrefix = str.substring(disPos + len, str.indexOf(",")).trim();
			System.out.println("str distinctPrefix: " + distinctPrefix);
		}

		disPos = str1.toUpperCase().indexOf("DISTINCT");
		if (disPos != -1) {
			String distinctPrefix = str1.substring(disPos + len, str1.indexOf(",")).trim();
			System.out.println("str1 distinctPrefix: " + distinctPrefix);
		}

		final String json = "{\"commit\" : \"http://localhost:7474/db/data/transaction/1/commit\",\"columns\" : [ \"n\",\"m\" ]}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

		List<String> columnList = (List) jsonMap.get("columns");
		for (Object column : columnList) {
			System.out.println("column:{} " + column);
		}
	}

}
