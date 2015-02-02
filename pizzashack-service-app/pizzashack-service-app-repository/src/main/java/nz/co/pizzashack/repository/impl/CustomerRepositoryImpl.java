package nz.co.pizzashack.repository.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.RelationshipsLabel;
import nz.co.pizzashack.repository.CustomerRepository;
import nz.co.pizzashack.repository.convert.CustomerModelToCreateStatement;
import nz.co.pizzashack.repository.convert.CustomerModelToMap;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.fetch.CustomerFetchStrategy;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class CustomerRepositoryImpl extends RepositoryBase<Customer, String> implements CustomerRepository {

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("customerMetaMapToModelConverter")
	private Function<Map<String, String>, Customer> customerMetaMapToModelConverter;

	public CustomerRepositoryImpl() {
		super("Person", "customerNo");
	}

	@Override
	public String create(final Customer addCustomer) throws Exception {
		return this.createUnique(addCustomer, new CustomerModelToCreateStatement());
	}

	@Override
	public Customer getByCustomerNo(final String customerNo) throws NotFoundException {
		return this.getBasicById(customerNo, customerMetaMapToModelConverter);
	}

	@Override
	public Customer getByCustomerNoWithFetchStrategy(final String customerNo, final CustomerFetchStrategy fetchStrategy) throws NotFoundException {
		checkArgument(!StringUtils.isEmpty(customerNo), "customerNo can not be null");
		AbstractCypherQueryResult result = this.doGetBasicById(customerNo);
		if (result == null) {
			throw new NotFoundException("Entity not found by id[" + customerNo + "].");
		}
		switch (fetchStrategy) {
		case ALL:
			break;
		case NONE:
			return this.getByCustomerNo(customerNo);
		case USER:
			break;
		case ORDERS:
			break;
		default:
			return this.getByCustomerNo(customerNo);
		}
		return null;
	}

	@Override
	public Set<Customer> getAll() throws Exception {
		return this.getBasicAll(customerMetaMapToModelConverter);
	}

	@Override
	public Page<Customer> paginateAll(int pageOffset, int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, customerMetaMapToModelConverter);
	}

	@Override
	public void update(final Customer updateCustomer) throws Exception {
		this.updateBasicById(updateCustomer, new CustomerModelToMap());
	}

	@Override
	public void deleteByCustomerNo(String customerNo) throws Exception {
		this.deleteAllById(customerNo, true);
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public void assignUser(final String customerNodeUri, final String userNodeUri) throws Exception {
		checkArgument(!StringUtils.isEmpty(customerNodeUri), "customerNodeUri can not be null");
		checkArgument(!StringUtils.isEmpty(userNodeUri), "userNodeUri can not be null");
		neo4jRestAPIAccessor.buildRelationshipBetween2Nodes(customerNodeUri, userNodeUri, RelationshipsLabel.HasUser.name());
	}

}
