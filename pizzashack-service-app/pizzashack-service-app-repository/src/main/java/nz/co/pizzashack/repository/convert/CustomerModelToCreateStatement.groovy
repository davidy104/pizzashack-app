package nz.co.pizzashack.repository.convert

import static nz.co.pizzashack.util.GenericUtils.formatDate

import java.lang.reflect.Field

import nz.co.pizzashack.model.Customer

import com.google.common.base.Function
import com.google.common.base.Joiner

class CustomerModelToCreateStatement implements Function<Customer, String>{

	@Override
	public String apply(final Customer customer) {
		Field[] fields = customer.getClass().getDeclaredFields()
		Class superClz = customer.getClass().getSuperclass()
		if(superClz){
			fields += superClz.getDeclaredFields()
		}
		String resultString
		if(fields){
			List fieldValueList = []
			fields.each {
				it.setAccessible(true)
				def val = it.get(customer)
				if(val){
					if(it.getName() == 'createTime'){
						val = formatDate('yyyy-MM-dd hh:mm:ss', (Date)val)
					} else if(it.getName() == 'birthDate'){
						val = formatDate('yyyy-MM-dd', (Date)val)
					}
					def str = "${it.name}:'${val}'"
					fieldValueList << str
				}
			}
			return Joiner.on(",").join(fieldValueList)
		}
	}
}
