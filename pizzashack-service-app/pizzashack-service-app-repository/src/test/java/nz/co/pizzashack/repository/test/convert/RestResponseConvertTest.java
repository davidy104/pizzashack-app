package nz.co.pizzashack.repository.test.convert;

import static nz.co.pizzashack.repository.test.TestUtils.readFileAsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import java.math.BigDecimal;
import java.util.Map;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestResponseConvertTest {

	private static final String TEST_CYPHER_TRANS_REST_JSON = "cypherTransRestResponse.json";
	private static Map<String, Object> cypherTransRestResponseMap = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseConvertTest.class);

	private Neo4jRestGenericConverter neo4jRestGenericConverter;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		cypherTransRestResponseMap = objectMapper.readValue(readFileAsString(TEST_CYPHER_TRANS_REST_JSON), Map.class);
		assertNotNull(cypherTransRestResponseMap);
	}

	@Before
	public void init() {
		neo4jRestGenericConverter = new Neo4jRestGenericConverter(new JsonBuilder(), new JsonSlurper());
	}

	@Test
	public void testConertCypherTransRestResponse() {
		final String expectedNodeUri = "http://localhost:7474/db/data/node/0";
		final String expectedNodeColumn = "n";

		Map<String, String> convertedMap = neo4jRestGenericConverter.transCypherRestFormatResponseConvert(cypherTransRestResponseMap);
		assertNotNull(convertedMap);
		final String key = (String) convertedMap.keySet().toArray()[0];
		final String value = (String) convertedMap.values().toArray()[0];

		assertEquals(expectedNodeUri, value);
		assertEquals(expectedNodeColumn, key);
		LOGGER.info("column:{} ", key);
		LOGGER.info("uri:{} ", value);
	}

	@Test
	public void testCreateStatementForObject() {
		Pizzashack pizzashack = new Pizzashack.Builder().pizzaName("abc").description("abc pizzashack").price(new BigDecimal("66.55")).build();
		String convertedString = neo4jRestGenericConverter.modelToCreateStatement(pizzashack, "Pizzashack", "p");
		LOGGER.info("convertedString:{} ", convertedString);
		
		
		String uniqueReqJson = neo4jRestGenericConverter.buildUniqueNodeRequest("acd","ddfdf","12345");
		LOGGER.info("uniqueReqJson:{} ", uniqueReqJson);
	}

}
