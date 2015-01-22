package nz.co.pizzashack.repository.convert.component;

import nz.co.pizzashack.model.Pizzashack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

public class PizzashackModelValueToString implements Function<Pizzashack, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackModelValueToString.class);

	@Override
	public String apply(final Pizzashack pizzashack) {
		String[] fieldValuePairArray = new String[] { "id:" + pizzashack.getPizzashackId(),
				"description:" + pizzashack.getDescription(), "name" + pizzashack.getPizzaName(), "price:" + pizzashack.getPrice().toString()
				, "icone:" + pizzashack.getIcon() };
		return Joiner.on(",").join(fieldValuePairArray);
	}

}
