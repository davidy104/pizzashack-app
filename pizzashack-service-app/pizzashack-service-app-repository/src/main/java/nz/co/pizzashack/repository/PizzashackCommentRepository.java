package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.PizzashackCommentType;

public interface PizzashackCommentRepository {

	String createPizzashackComment(String pizzashackNodeUri, String userNodeUri, PizzashackComment comment) throws Exception;

	Set<PizzashackComment> getAllByPizzashackId(String pizzashackId) throws Exception;

	Set<PizzashackComment> getAllByUserName(String userName) throws Exception;

	PizzashackComment getByPizzashackIdAndUserName(String pizzashackId, String userName) throws Exception;

	void deleteCommentByUserName(String userName) throws Exception;

	void deleteCommentByPizzashackId(String pizzashackId) throws Exception;
	
	Long countCommentsByPizzashackId(String pizzashackId,PizzashackCommentType commentType)throws Exception;

}
