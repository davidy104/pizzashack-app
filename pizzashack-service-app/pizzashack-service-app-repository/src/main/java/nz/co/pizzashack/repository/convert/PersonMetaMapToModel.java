package nz.co.pizzashack.repository.convert;

import java.util.Map;

import nz.co.pizzashack.model.Person;

import com.google.common.base.Function;

public class PersonMetaMapToModel implements Function<Map<String, String>, Person> {

	@Override
	public Person apply(final Map<String, String> inputMap) {
		Person person = null;
		if(null != inputMap){
		}
		return person;
	}

}
