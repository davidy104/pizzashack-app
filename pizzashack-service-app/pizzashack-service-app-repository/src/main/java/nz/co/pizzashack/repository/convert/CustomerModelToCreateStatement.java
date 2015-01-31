package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.List;

import nz.co.pizzashack.model.Customer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class CustomerModelToCreateStatement implements Function<Customer, String>{

	@Override
	public String apply(final Customer customer) {
		if(customer != null){
			List<String> fieldValueList = Lists.<String>newArrayList();
			fieldValueList.add("customerNo:'"+customer.getCustomerNo()+"'");
			fieldValueList.add("email:'"+customer.getEmail()+"'");
			fieldValueList.add("firstName:'"+customer.getFirstName()+"'");
			fieldValueList.add("lastName:'"+customer.getLastName()+"'");
			if(customer.getBirthDate()!=null){
				fieldValueList.add("birthDate:'"+formatDate("yyyy-MM-dd",customer.getBirthDate())+"'");
			}
			
			if(customer.getCreateTime()!=null){
				fieldValueList.add("createTime:'"+formatDate("yyyy-MM-dd hh:mm:ss",customer.getCreateTime())+"'");
			}
			return Joiner.on(",").join(fieldValueList);
		}
		return null;
	}
}
