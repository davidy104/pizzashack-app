package nz.co.pizzashack.model.convert;

import java.io.IOException;

import nz.co.pizzashack.model.Pizzashack;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PizzashackJsonDeserializer extends JsonDeserializer<Pizzashack> {

	@Override
	public Pizzashack deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		Pizzashack result = new Pizzashack.Builder()
				.pizzaName(node.get("pizzaName").textValue())
				.description(node.get("description").textValue())
				.icon(node.get("icon").textValue())
				.build();

		if (node.get("price") != null) {
			result.setPrice(node.get("price").decimalValue());
		}

		return result;
	}

}
