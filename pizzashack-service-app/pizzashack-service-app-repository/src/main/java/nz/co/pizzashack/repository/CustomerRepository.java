package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Page;

public interface CustomerRepository {

	String create(Customer addCustomer) throws Exception;

	Customer getByCustomerNo(String customerNo) throws Exception;

	Set<Customer> getAll() throws Exception;

	Page<Customer> paginateAll() throws Exception;

	void update(Customer updateCustomer) throws Exception;

	void deleteByCustomerNo(String customerNo) throws Exception;
}
