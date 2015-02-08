package nz.co.pizzashack.repository.impl;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.repository.PizzashackRepository;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * 
 * @author Davidy
 *
 */
public class PizzashackRepositoryImpl extends RepositoryBase<Pizzashack, String> implements PizzashackRepository {

//	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("pizzashackMetaMapToModelConverter")
	private Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter;

	public PizzashackRepositoryImpl() {
		super("Pizzashack", "pizzashackId");
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public String create(final Pizzashack addPizzashack) throws Exception {
		return this.createUnique(addPizzashack, new Function<Pizzashack,String>(){
			@Override
			public String apply(final Pizzashack pizzashack) {
				if(pizzashack != null){
					List<String> fieldValueList = Lists.<String>newArrayList();
					fieldValueList.add("pizzashackId:'"+pizzashack.getPizzashackId()+"'");
					fieldValueList.add("pizzaName:'"+pizzashack.getPizzaName()+"'");
					fieldValueList.add("description:'"+pizzashack.getDescription()+"'");
					fieldValueList.add("icon:'"+pizzashack.getIcon()+"'");
					if(pizzashack.getPrice() != null){
						fieldValueList.add("price:'"+pizzashack.getPrice()+"'");
					}
					if(pizzashack.getCreateTime()!=null){
						fieldValueList.add("createTime:'"+formatDate("yyyy-MM-dd hh:mm:ss",pizzashack.getCreateTime())+"'");
					}
					if(pizzashack.getAmount() != null){
						fieldValueList.add("amount:'"+pizzashack.getAmount()+"'");
					}
					return Joiner.on(",").join(fieldValueList);
				}
				return null;
			}
		});
	}

	@Override
	public Pizzashack getById(final String pizzashackId) throws Exception {
		return this.getBasicById(pizzashackId, pizzashackMetaMapToModelConverter);
	}

	@Override
	public void update(final Pizzashack updatePizzashack) throws Exception {
		this.updateBasicById(updatePizzashack);
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
