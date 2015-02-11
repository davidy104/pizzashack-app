package nz.co.pizzashack.service;

import java.io.InputStream;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackDS {

	String createPizzashack(Pizzashack addPizzashack, String imageName, InputStream imageStream) throws Exception;

	void deleteById(String pizzashackId) throws Exception;

	Pizzashack getPizzashackById(String pizzashackId) throws NotFoundException;

	void updatePizzashack(String pizzashackId, Pizzashack updatePizzashack) throws Exception;

	Set<Pizzashack> getAllPizzashack();

	Page<Pizzashack> paginatePizzashack(int pageOffset, int pageSize);

}
