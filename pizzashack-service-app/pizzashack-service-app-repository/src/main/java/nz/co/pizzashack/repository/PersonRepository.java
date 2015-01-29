package nz.co.pizzashack.repository;

import java.util.Set;

import nz.co.pizzashack.model.Person;

public interface PersonRepository {

	String create(Person addPerson) throws Exception;

	void update(Person updatePerson) throws Exception;

	void deleteByEmail(String email) throws Exception;

	Set<Person> getByName(String firstName, String lastName) throws Exception;

	Person getByEmail(String email) throws Exception;

	Set<Person> getAll() throws Exception;
}
