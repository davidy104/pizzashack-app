package nz.co.pizzashack.repository.impl;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author Davidy
 *
 */
public class PizzashackRepositoryImpl extends RepositoryBase<Pizzashack, String> implements PizzashackRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("pizzashackQueryNodeToModelConverter")
	private Function<AbstractCypherQueryNode, Pizzashack> pizzashackQueryNodeToModelConverter;

	@Inject
	@Named("pizzashackModelToJsonConverter")
	private Function<Pizzashack, String> pizzashackModelToJsonConverter;

	@Inject
	@Named("pizzashackMetaMapToModelConverter")
	private Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter;

	public PizzashackRepositoryImpl() {
		super("pizzashackId", Pizzashack.class);
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public Pizzashack getById(final String pizzashackId) throws Exception {
		return this.getBasicById(pizzashackId, pizzashackMetaMapToModelConverter);
	}

	@Override
	public void updateById(final String pizzashackId, final Pizzashack updatePizzashack) throws Exception {
		this.updateBasicById(updatePizzashack, pizzashackModelToJsonConverter);
	}

	@Override
	public void deleteById(final String pizzashackId) throws Exception {
		this.deleteAllById(pizzashackId, true);
	}

	@Override
	public Set<Pizzashack> getAll() throws Exception {
		LOGGER.info("getAllPizzashack start..");
		return this.getBasicAll(pizzashackMetaMapToModelConverter);
	}

	@Override
	public Page<Pizzashack> paginateAll(final int pageOffset, final int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, pizzashackMetaMapToModelConverter);
	}

}
