package nz.co.pizzashack.repository.impl;

import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

public class PizzashackCommentRepositoryImpl extends RepositoryBase<PizzashackComment, String> implements PizzashackCommentRepository{

	public PizzashackCommentRepositoryImpl() {
		super("PizzashackComment", null);
	}

	@Override
	public String createPizzashackComment(String pizzashackId, String userName,
			PizzashackComment comment) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PizzashackComment> getAllByPizzashackId(String pizzashackId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<PizzashackComment> getAllByUserName(String userName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<PizzashackComment> paginateByPizzashackIdAndUserName(
			int pageOffset, int pageSize, String pizzashackId, String userName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteComment(String userName, String pizzashackId)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
