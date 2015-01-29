package nz.co.pizzashack.test.convert;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;

import java.util.Date;
import java.util.UUID;

import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Person;
import nz.co.pizzashack.repository.convert.template.Neo4jRestGenericConverter;

import org.junit.Test;

public class Neo4jRestGenericConverterTest {
	private Neo4jRestGenericConverter neo4jRestGenericConverter = new Neo4jRestGenericConverter(new JsonBuilder(), new JsonSlurper());

	@Test
	public void testModelToCreateStatement() {
		Customer customer = new Customer.Builder().firstName("dav").lastName("yuan")
				.email("david.yuan@gmail.com")
				.customerNo(UUID.randomUUID().toString()).createTime(new Date()).build();

		Person customerPerson = (Person) customer;
		String createStatement = neo4jRestGenericConverter.modelToCreateStatement(customerPerson, "Person", "p");

		System.out.println(createStatement);
	}

}
