package nz.co.pizzashack.ds;

import java.io.InputStream;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;

import com.amazonaws.services.s3.model.S3Object;

public interface PizzashackDS {

	String createPizzashack(Pizzashack addPizzashack, String imageName, InputStream imageStream) throws Exception;

	void deleteById(String pizzashackId) throws Exception;

	Pizzashack getPizzashackById(String pizzashackId) throws NotFoundException;

	void updatePizzashack(String pizzashackId, Pizzashack updatePizzashack) throws Exception;

	Set<Pizzashack> getAllPizzashack();

	Page<Pizzashack> paginatePizzashack(Integer pageOffset, Integer pageSize);

	Page<Pizzashack> paginatePizzashackByName(Integer pageOffset, Integer pageSize, String pizzashackName);

	S3Object loadImageFromS3(String imageName) throws Exception;
}