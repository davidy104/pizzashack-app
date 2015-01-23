package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackRepository {

	String createPizzashack(String pizzashackId, Pizzashack addPizzashack) throws Exception;

	Pizzashack getPizzashackById(String pizzashackId) throws Exception;

	void updatePizzashack(String pizzashackId, Pizzashack updatePizzashack) throws Exception;

	void deletePizzashack(String pizzashackId) throws Exception;

	Set<Pizzashack> getAllPizzashack() throws Exception;
}
