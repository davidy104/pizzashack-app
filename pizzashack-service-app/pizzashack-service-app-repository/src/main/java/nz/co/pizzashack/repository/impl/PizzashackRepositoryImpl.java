package nz.co.pizzashack.repository.impl;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.convert.PizzashackNeo4jRestConverter;

import com.google.inject.Inject;

public class PizzashackRepositoryImpl implements PizzashackRepository {

	@Inject
	private PizzashackNeo4jRestConverter pizzashackNeo4jRestConverter;

	@Override
	public String createPizzashack(final String pizzashackId, final Pizzashack addPizzashack) throws Exception {
		final String createStatement = pizzashackNeo4jRestConverter.pizzashackModelToCreateStatement(addPizzashack);
		return null;
	}

}
