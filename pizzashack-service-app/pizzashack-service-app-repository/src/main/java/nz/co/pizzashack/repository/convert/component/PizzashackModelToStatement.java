package nz.co.pizzashack.repository.convert.component;

import nz.co.pizzashack.model.Pizzashack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

public class PizzashackModelToStatement implements Function<Pizzashack, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackModelToStatement.class);

	private Function<Pizzashack, String> pizzashackModelValueToString;

	public PizzashackModelToStatement(final Function<Pizzashack, String> pizzashackModelValueToString) {
		this.pizzashackModelValueToString = pizzashackModelValueToString;
	}

	@Override
	public String apply(final Pizzashack pizzashack) {
		StringBuilder builder = new StringBuilder("CREATE (p:Pizzashack{");
		builder.append(pizzashackModelValueToString.apply(pizzashack));
		builder.append("}) RETURN p");
		final String statement = builder.toString();
		LOGGER.info("statement:{} ", statement);
		return statement;
	}

}
