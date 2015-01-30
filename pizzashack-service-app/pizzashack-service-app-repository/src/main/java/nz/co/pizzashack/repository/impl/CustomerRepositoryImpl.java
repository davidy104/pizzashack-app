package nz.co.pizzashack.repository.impl;

import java.util.Set;

import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.Person;
import nz.co.pizzashack.repository.CustomerRepository;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

public class CustomerRepositoryImpl extends RepositoryBase<Person, String> implements CustomerRepository {

	public CustomerRepositoryImpl() {
		super("customerNo", Person.class);
	}

	@Override
	public String create(Customer addCustomer) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getByCustomerNo(String customerNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Customer> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Customer> paginateAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Customer updateCustomer) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByCustomerNo(String customerNo) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		// TODO Auto-generated method stub
		return null;
	}

}
