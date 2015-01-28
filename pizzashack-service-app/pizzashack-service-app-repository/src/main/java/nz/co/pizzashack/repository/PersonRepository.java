package nz.co.pizzashack.repository;

import nz.co.pizzashack.model.Person;

public interface PersonRepository {

	String create(Person addPerson) throws Exception;

}
