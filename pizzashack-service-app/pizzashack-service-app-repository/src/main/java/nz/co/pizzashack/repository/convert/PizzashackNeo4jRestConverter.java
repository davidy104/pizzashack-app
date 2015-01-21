package nz.co.pizzashack.repository.convert;

import nz.co.pizzashack.model.Pizzashack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PizzashackNeo4jRestConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackNeo4jRestConverter.class);

	@Inject
	@Named("pizzashackModelToStatement")
	private Function<Pizzashack, String> pizzashackModelToStatement;

	@Inject
	@Named("createStatementsToJson")
	private Function<String[], String> createStatementsToJson;

	public String pizzashackModelToCreateStatement(final Pizzashack model) {
		return pizzashackModelToStatement.apply(model);
	}

	public String pizzashackModelToCreateStatementJson(final Pizzashack model) {
		final String pizzaStatementStr = this.pizzashackModelToCreateStatement(model);
		return createStatementsToJson.apply(new String[] { pizzaStatementStr });
	}
}
