package nz.co.pizzashack.repository;

import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackRepository {

	String createPizzashack(String pizzashackId, Pizzashack addPizzashack) throws Exception;

	void updatePizzashack(String pizzashackId, Pizzashack updatePizzashack) throws Exception;
}
