package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.List;

import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class PizzashackModelToCreateStatement implements Function<Pizzashack, String> {

	@Override
	public String apply(final Pizzashack pizzashack) {
		if (pizzashack != null) {
			List<String> fieldValueList = Lists.<String> newArrayList();
			fieldValueList.add("pizzashackId:'" + pizzashack.getPizzashackId() + "'");
			fieldValueList.add("pizzaName:'" + pizzashack.getPizzaName() + "'");
			fieldValueList.add("description:'" + pizzashack.getDescription() + "'");
			fieldValueList.add("icon:'" + pizzashack.getIcon() + "'");
			fieldValueList.add("viewed:'" + pizzashack.getViewed() + "'");
			if (pizzashack.getPrice() != null) {
				fieldValueList.add("price:'" + pizzashack.getPrice() + "'");
			}
			if (pizzashack.getCreateTime() != null) {
				fieldValueList.add("createTime:'" + formatDate("yyyy-MM-dd hh:mm:ss", pizzashack.getCreateTime()) + "'");
			}
			if (pizzashack.getAmount() != null) {
				fieldValueList.add("amount:'" + pizzashack.getAmount() + "'");
			}
			return Joiner.on(",").join(fieldValueList);
		}
		return null;
	}

}
