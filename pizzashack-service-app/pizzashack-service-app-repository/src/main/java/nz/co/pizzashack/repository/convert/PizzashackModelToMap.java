package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Map;

import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class PizzashackModelToMap implements Function<Pizzashack, Map<String, String>> {

	@Override
	public Map<String, String> apply(final Pizzashack pizzashack) {
		Map<String, String> resultMap = Maps.<String, String> newHashMap();
		if (pizzashack != null) {
			resultMap.put("pizzashackName", pizzashack.getPizzaName());
			resultMap.put("icon", pizzashack.getIcon());
			resultMap.put("description", pizzashack.getDescription());
			resultMap.put("pizzashackId", pizzashack.getPizzashackId());
			resultMap.put("viewed", String.valueOf(pizzashack.getViewed()));
			if (pizzashack.getPrice() != null) {
				resultMap.put("price", String.valueOf(pizzashack.getPrice()));
			}
			if (pizzashack.getCreateTime() != null) {
				resultMap.put("createTime", formatDate("yyyy-MM-dd hh:mm:ss", pizzashack.getCreateTime()));
			}
			if (pizzashack.getAmount() != null) {
				resultMap.put("amount", String.valueOf(pizzashack.getAmount()));
			}
		}
		return resultMap;
	}

}
