package nz.co.pizzashack.test.repository;

import java.util.Date;
import java.util.UUID;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.repository.CustomerRepository;
import nz.co.pizzashack.repository.RepositoryModule;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class, RepositoryModule.class })
public class CustomerRepositoryIntegrationTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepositoryIntegrationTest.class);

	@Inject
	private CustomerRepository customerRepository;

	@Test
	public void testCreate() throws Exception {
		final String customerNo = "CUST-" + UUID.randomUUID().toString();
		Customer customer = new Customer.Builder()
				.email("david.yuan@gmail.com")
				.customerNo(customerNo)
				.lastName("Yuan")
				.firstName("David")
				.createTime(new Date()).build();
		final String nodeUri = customerRepository.create(customer);
		LOGGER.info("nodeUri:{} ", nodeUri);

		customer = customerRepository.getByCustomerNo(customerNo);
		LOGGER.info("found customer:{} ", customer);
	}

}
