package nz.co.pizzashack.repository.impl;

import java.util.Set;

import nz.co.pizzashack.model.Person;
import nz.co.pizzashack.repository.PersonRepository;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import com.google.inject.Inject;

public class PersonRepositoryImpl extends RepositoryBase<Person, String> implements PersonRepository {

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	public PersonRepositoryImpl() {
		super("email", Person.class);
	}

	@Override
	public void update(final Person updatePerson) throws Exception {
		this.updateBasic(updatePerson);
	}

	@Override
	public void deleteByEmail(String email) throws Exception {
		this.deleteAllById(email, true);
	}

	@Override
	public Set<Person> getByName(final String firstName, final String lastName) throws Exception {
		return null;
	}

	@Override
	public Person getByEmail(final String email) throws Exception {
		return null;
	}

	@Override
	public Set<Person> getAll() throws Exception {
		return null;
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

}
