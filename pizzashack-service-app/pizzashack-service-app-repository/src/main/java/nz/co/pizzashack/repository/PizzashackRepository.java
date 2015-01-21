package nz.co.pizzashack.repository;

import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackRepository {

	String createPizzashack(String pizzashackId, Pizzashack addPizzashack) throws Exception;
}
