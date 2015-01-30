package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.util.Map;

import nz.co.pizzashack.model.Customer;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class CustomerMetaMapToModel implements Function<Map<String, String>, Customer> {

	@Override
	public Customer apply(final Map<String, String> inputMap) {
		Customer customer = null;
		if (inputMap != null) {
			customer = new Customer.Builder()
					.lastName(inputMap.get("lastName"))
					.firstName(inputMap.get("firstName"))
					.customerNo(inputMap.get("customerNo"))
					.email(inputMap.get("email"))
					.build();

			if (!StringUtils.isEmpty(inputMap.get("birthDate"))) {
				customer.setBirthDate(parseToDate("yyyy-MM-dd", inputMap.get("birthDate")));
			}

			if (!StringUtils.isEmpty(inputMap.get("createTime"))) {
				customer.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", inputMap.get("createTime")));
			}
		}
		return customer;
	}
}
