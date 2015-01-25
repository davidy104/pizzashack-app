package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackRepository {

	String create(String pizzashackId, Pizzashack addPizzashack) throws Exception;

	Pizzashack getById(String pizzashackId) throws Exception;

	void updateById(String pizzashackId, Pizzashack updatePizzashack) throws Exception;

	void deleteById(String pizzashackId) throws Exception;

	Set<Pizzashack> getAll() throws Exception;
	
	Page<Pizzashack> paginateAll(int pageOffset,int pageSize)throws Exception;
}
