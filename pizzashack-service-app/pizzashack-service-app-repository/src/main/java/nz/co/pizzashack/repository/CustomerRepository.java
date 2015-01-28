package nz.co.pizzashack.repository;

import nz.co.pizzashack.model.Customer;

public interface CustomerRepository {

	String create(Customer addCustomer) throws Exception;

	Customer getByCustomerNo(String customerNo) throws Exception;
}
