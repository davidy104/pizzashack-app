package nz.co.pizzashack.repository.impl;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.repository.CustomerRepository;
import nz.co.pizzashack.repository.convert.CustomerModelToCreateStatement;
import nz.co.pizzashack.repository.convert.CustomerModelToMap;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

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
	public Customer getByCustomerNo(final String customerNo) throws Exception {
		return this.getBasicById(customerNo, customerMetaMapToModelConverter);
	}

	@Override
	public Set<Customer> getAll() throws Exception {
		return this.getBasicAll(customerMetaMapToModelConverter);
	}

	@Override
	public Page<Customer> paginateAll() throws Exception {
		return null;
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

}
