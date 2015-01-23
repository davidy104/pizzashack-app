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
		String str2 = "MATCH (book:Book{isbn:{isbn}}) RETURN Distinct book";

		int len = "DISTINCT".length();

		int disPos = str.toUpperCase().indexOf("DISTINCT");
		String distinctPrefix = null;

		if (disPos != -1) {
			distinctPrefix = str.substring(disPos + len, str.indexOf(",")).trim();
			System.out.println("str distinctPrefix: " + distinctPrefix);
		}

		disPos = str1.toUpperCase().indexOf("DISTINCT");
		if (disPos != -1) {
			distinctPrefix = str1.substring(disPos + len, str1.indexOf(",")).trim();
			System.out.println("str1 distinctPrefix: " + distinctPrefix);
		}

		disPos = str2.toUpperCase().indexOf("DISTINCT");
		if (disPos != -1) {
			if (str2.indexOf(",") != -1 && str2.indexOf(",") > disPos) {
				distinctPrefix = str2.substring(disPos + len, str2.indexOf(",")).trim();
			} else {
				distinctPrefix = str2.substring(disPos + len, str2.length()).trim();
			}

			System.out.println("str2 distinctPrefix: " + distinctPrefix);
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
