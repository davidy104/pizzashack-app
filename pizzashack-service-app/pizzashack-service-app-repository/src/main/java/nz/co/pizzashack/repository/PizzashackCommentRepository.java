package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;

public interface PizzashackCommentRepository {

	String createPizzashackComment(String pizzashackId,String userName,PizzashackComment comment)throws Exception;
	
	Set<PizzashackComment> getAllByPizzashackId(String pizzashackId)throws Exception;
	
	Set<PizzashackComment> getAllByUserName(String userName)throws Exception;
	
	Page<PizzashackComment> paginateByPizzashackIdAndUserName(int pageOffset,int pageSize,String pizzashackId,String userName)throws Exception;
	
	void deleteComment(String userName,String pizzashackId)throws Exception;

}
