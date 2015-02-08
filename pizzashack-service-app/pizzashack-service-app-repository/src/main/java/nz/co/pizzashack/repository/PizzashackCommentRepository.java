package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.PizzashackComment;

public interface PizzashackCommentRepository {

	String createPizzashackComment(String pizzashackId,String userId,PizzashackComment comment)throws Exception;
	
	Set<PizzashackComment> getAllByPizzashackId(String pizzashackId)throws Exception;
	
	Set<PizzashackComment> getAllByUserId(String userId)throws Exception;

}
