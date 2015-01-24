package nz.co.pizzashack.test.repository;

import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
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
public class PizzashackRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryIntegrationTest.class);

	@Inject
	private PizzashackRepository pizzashackRepository;

	@Test
	public void testGetAll() throws Exception {
		final String pizzashackId = UUID.randomUUID().toString();
		final String nodeUri = pizzashackRepository.createPizzashack(pizzashackId, new Pizzashack.Builder().pizzaName("testPizzaname").pizzashackId(pizzashackId).description("testDesc").build());
		LOGGER.info("nodeUri:{} ", nodeUri);
		Set<Pizzashack> allPizzashacks = pizzashackRepository.getAllPizzashack();
		for (final Pizzashack pizzashack : allPizzashacks) {
			LOGGER.info("pizzashack:{} ", pizzashack);
		}

//		Pizzashack found = pizzashackRepository.getPizzashackById(pizzashackId);
//		LOGGER.info("found---------------:{} ", found);
//
//		found.setDescription("updateDesc");
//		found.setPizzaName("updatePizzaname");
//		found.setIcon("updateIcon");
//		pizzashackRepository.updatePizzashack(pizzashackId, found);
//
//		found = pizzashackRepository.getPizzashackById(pizzashackId);
//		LOGGER.info("after update---------------:{} ", found);

		pizzashackRepository.deletePizzashack(pizzashackId);
	}

}
