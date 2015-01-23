package nz.co.pizzashack.repository.convert.component;

import java.math.BigDecimal;
import java.util.Map;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class PizzashackQueryNodeToModel implements Function<AbstractCypherQueryNode, Pizzashack> {

	@Override
	public Pizzashack apply(final AbstractCypherQueryNode node) {
		Pizzashack result = null;
		if (node != null) {
			Map<String, String> fieldValMap = node.getDataMap();
			result = new Pizzashack.Builder().nodeUri(node.getUri())
					.pizzaName(fieldValMap.get("pizzaName"))
					.pizzashackId(fieldValMap.get("pizzashackId"))
					.description(fieldValMap.get("description"))
					.icon(fieldValMap.get("icon"))
					.build();

			final String priceStr = fieldValMap.get("price");
			if (!StringUtils.isEmpty(priceStr)) {
				result.setPrice(new BigDecimal(priceStr));
			}
		}
		return result;
	}

}
