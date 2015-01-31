package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Map;

import nz.co.pizzashack.model.Customer;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class CustomerModelToMap implements Function<Customer,Map<String,String>> {

	@Override
	public Map<String, String> apply(final Customer customer) {
		Map<String,String> resultMap = Maps.<String,String>newHashMap();
		if(customer != null){
			resultMap.put("customerNo", customer.getCustomerNo());
			resultMap.put("email", customer.getEmail());
			resultMap.put("firstName", customer.getFirstName());
			resultMap.put("lastName", customer.getLastName());
			
			if(customer.getBirthDate()!=null){
				resultMap.put("birthDate", formatDate("yyyy-MM-dd",customer.getBirthDate()));
			}
			
			if(customer.getCreateTime()!=null){
				resultMap.put("createTime", formatDate("yyyy-MM-dd hh:mm:ss",customer.getCreateTime()));
			}
		}
		return resultMap;
	}

}
