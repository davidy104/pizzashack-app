package nz.co.pizzashack.repository.test.convert;

import static nz.co.pizzashack.repository.test.TestUtils.readFileAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import nz.co.pizzashack.repository.convert.template.TransCypherRestFormatResponseConvert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestResponseConvertTest {

	private static final String TEST_CYPHER_TRANS_REST_JSON = "cypherTransRestResponse.json";
	private static Map<String, Object> cypherTransRestResponseMap = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseConvertTest.class);

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		cypherTransRestResponseMap = objectMapper.readValue(readFileAsString(TEST_CYPHER_TRANS_REST_JSON), Map.class);
		assertNotNull(cypherTransRestResponseMap);
	}

	@Test
	public void testConertCypherTransRestResponse() {
		final String expectedNodeUri = "http://localhost:7474/db/data/node/0";
		final String expectedNodeColumn = "n";
		TransCypherRestFormatResponseConvert converter = new TransCypherRestFormatResponseConvert();
		Map<String, String> convertedMap = converter.apply(cypherTransRestResponseMap);
		assertNotNull(convertedMap);
		final String key = (String) convertedMap.keySet().toArray()[0];
		final String value = (String) convertedMap.values().toArray()[0];

		assertEquals(expectedNodeUri, value);
		assertEquals(expectedNodeColumn, key);
		LOGGER.info("column:{} ", key);
		LOGGER.info("uri:{} ", value);
	}

}
