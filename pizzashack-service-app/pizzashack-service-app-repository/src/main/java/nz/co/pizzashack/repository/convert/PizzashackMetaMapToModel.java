package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.parseToDate;

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
			
			final String amountStr = metaMap.get("amount");
			if(!StringUtils.isEmpty(amountStr)){
				result.setAmount(Integer.valueOf(amountStr));
			}
			
			final String createTimeStr = metaMap.get("createTime");
			if(!StringUtils.isEmpty(createTimeStr)){
				result.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", createTimeStr));
			}
			final String viewedStr = metaMap.get("viewed");
			if(!StringUtils.isEmpty(viewedStr)){
				result.setViewed(Long.valueOf(viewedStr));
			}
		}
		return result;
	}

}
