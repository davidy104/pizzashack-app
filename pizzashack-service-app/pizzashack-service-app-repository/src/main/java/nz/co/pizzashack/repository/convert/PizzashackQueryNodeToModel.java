package nz.co.pizzashack.repository.convert;

import java.util.Map;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;

import com.google.common.base.Function;

public class PizzashackQueryNodeToModel implements Function<AbstractCypherQueryNode, Pizzashack> {

	private Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter;
	
	public PizzashackQueryNodeToModel(final Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter) {
		this.pizzashackMetaMapToModelConverter = pizzashackMetaMapToModelConverter;
	}

	@Override
	public Pizzashack apply(final AbstractCypherQueryNode node) {
		Pizzashack result = null;
		if (node != null) {
			Map<String, String> fieldValMap = node.getDataMap();
			result = pizzashackMetaMapToModelConverter.apply(fieldValMap);
			result.setNodeUri(node.getUri());
		}
		return result;
	}

}
