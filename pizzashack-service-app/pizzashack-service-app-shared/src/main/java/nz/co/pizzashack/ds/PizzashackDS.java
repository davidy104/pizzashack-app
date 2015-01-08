package nz.co.pizzashack.ds;

import java.util.Set;

import nz.co.pizzashack.ConflictException;
import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;

public interface PizzashackDS {
	Pizzashack createPizzashack(Pizzashack pizzashack) throws ConflictException;

	Pizzashack getPizzashackById(String pizzashackId) throws NotFoundException;

	Set<Pizzashack> getAllPizzashacks();

	Page<Pizzashack> paginatePizzashack(Integer pageOffset, Integer pageSize);

	void deleteById(String pizzashackId) throws Exception;

	Pizzashack updatePizzashack(String pizzashackId, Pizzashack updatePizzashack) throws Exception;

}
