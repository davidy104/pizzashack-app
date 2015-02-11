package nz.co.pizzashack.repository.impl;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author Davidy
 *
 */
public class PizzashackRepositoryImpl extends RepositoryBase<Pizzashack, String> implements PizzashackRepository {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("pizzashackMetaMapToModelConverter")
	private Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter;

	@Inject
	@Named("pizzashackModelToCreateStatementConverter")
	private Function<Pizzashack, String> pizzashackModelToCreateStatementConverter;

	@Inject
	@Named("pizzashackModelToMapConverter")
	private Function<Pizzashack, Map<String, String>> pizzashackModelToMapConverter;

	public PizzashackRepositoryImpl() {
		super("Pizzashack", "pizzashackId");
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public String create(final Pizzashack addPizzashack) throws Exception {
		return this.createUnique(addPizzashack, pizzashackModelToCreateStatementConverter);
	}

	@Override
	public Pizzashack getById(final String pizzashackId) throws NotFoundException {
		return this.getBasicById(pizzashackId, pizzashackMetaMapToModelConverter);
	}

	@Override
	public void update(final Pizzashack updatePizzashack) throws Exception {
		this.updateBasicById(updatePizzashack, pizzashackModelToMapConverter);
	}

	@Override
	public void deleteById(final String pizzashackId) throws Exception {
		this.deleteAllById(pizzashackId, true);
	}

	@Override
	public Set<Pizzashack> getAll() throws Exception {
		return this.getBasicAll(pizzashackMetaMapToModelConverter);
	}

	@Override
	public Page<Pizzashack> paginateAll(final int pageOffset, final int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, pizzashackMetaMapToModelConverter);
	}

}
