package nz.co.pizzashack.repository.convert;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Function;

public class PizzashackMetaMapToModel implements Function<Map<String, String>, Pizzashack> {

	@Override
	public Pizzashack apply(final Map<String, String> metaMap) {
		Pizzashack result = null;
		if (metaMap != null) {
			result = new Pizzashack.Builder()
					.pizzaName(metaMap.get("pizzaName"))
					.pizzashackId(metaMap.get("pizzashackId"))
					.description(metaMap.get("description"))
					.icon(metaMap.get("icon"))
					.build();

			final String priceStr = metaMap.get("price");
			if (!StringUtils.isEmpty(priceStr)) {
				result.setPrice(new BigDecimal(priceStr));
			}
		}
		return result;
	}

}
