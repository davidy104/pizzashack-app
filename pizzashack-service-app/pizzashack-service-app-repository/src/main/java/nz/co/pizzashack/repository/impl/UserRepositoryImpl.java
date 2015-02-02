package nz.co.pizzashack.repository.impl;

import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.UserRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class UserRepositoryImpl extends RepositoryBase<User, String> implements UserRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("userMetaMapToModelConverter")
	private Function<Map<String, String>, User> userMetaMapToModelConverter;

	public UserRepositoryImpl() {
		super("User", "userName");
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public String create(User addUser) throws Exception {
		return this.createUnique(addUser);
	}

	@Override
	public Set<User> getAll() throws Exception {
		return this.getBasicAll(userMetaMapToModelConverter);
	}

	@Override
	public User getByName(final String userName) throws Exception {
		return this.getBasicById(userName, userMetaMapToModelConverter);
	}

	/**
	 * { "query" :
	 * "(u:User{userName:{userName},password:{password}}) RETURN u", 
	 * "params" : { "password" : "{password}", "userName" : "{userName}" } 
	 * } 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User getByNameAndPwd(final String userName, final String password) throws Exception {
		User found = null;
		final String queryJson = "MATCH (u:User{userName:{userName},password:{password}}) RETURN u";

		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("password", password)
						.put("userName", userName)
						.build()));

		if (result != null) {
			Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("u");
			if (metaMap != null && !metaMap.isEmpty()) {
				final String nodeUri = (String) metaMap.keySet().toArray()[0];
				final Map<String, String> fieldValueMap = (Map<String, String>) metaMap.values().toArray()[0];
				found = userMetaMapToModelConverter.apply(fieldValueMap);
				found.setNodeUri(nodeUri);
			}
		}
		return found;
	}

	@Override
	public void update(final User updateUser) throws Exception {
		this.updateBasicById(updateUser);
	}

	@Override
	public void deleteByName(final String userName) throws Exception {
		this.deleteAllById(userName, true);
	}

	@Override
	public Page<User> paginateAll(final int pageOffset, final int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, userMetaMapToModelConverter);
	}

}
