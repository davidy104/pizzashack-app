package nz.co.pizzashack.repository.convert;

import groovy.json.JsonOutput
import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Function

public class PizzashackModelToJson implements Function<Pizzashack,String>{

	@Override
	public String apply(final Pizzashack pizzashack) {
		return JsonOutput.toJson([
			pizzashackId: pizzashack.pizzashackId,
			pizzaName: pizzashack.pizzaName,
			icon: pizzashack.icon,
			description: pizzashack.description,
			price:String.valueOf(pizzashack.price)])
	}
}
