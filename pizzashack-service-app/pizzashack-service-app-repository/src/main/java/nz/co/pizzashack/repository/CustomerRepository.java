package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.repository.fetch.CustomerFetchStrategy;

public interface CustomerRepository {

	String create(Customer addCustomer) throws Exception;

	Customer getByCustomerNo(String customerNo) throws NotFoundException;

	Set<Customer> getAll() throws Exception;

	Page<Customer> paginateAll(int pageOffset, int pageSize) throws Exception;

	void update(Customer updateCustomer) throws Exception;

	void deleteByCustomerNo(String customerNo) throws Exception;

	void assignUser(String customerNodeUri, String userNodeUri) throws Exception;

	Customer getByCustomerNoWithFetchStrategy(String customerNo, CustomerFetchStrategy fetchStrategy) throws NotFoundException;
}
