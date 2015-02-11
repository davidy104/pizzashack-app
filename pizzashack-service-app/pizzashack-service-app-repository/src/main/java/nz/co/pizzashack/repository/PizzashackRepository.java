package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;


public interface PizzashackRepository {

	String create(Pizzashack addPizzashack) throws Exception;

	Pizzashack getById(String pizzashackId) throws NotFoundException;

	void update(Pizzashack updatePizzashack) throws Exception;

	void deleteById(String pizzashackId) throws Exception;

	Set<Pizzashack> getAll() throws Exception;

	Page<Pizzashack> paginateAll(int pageOffset, int pageSize) throws Exception;
}
