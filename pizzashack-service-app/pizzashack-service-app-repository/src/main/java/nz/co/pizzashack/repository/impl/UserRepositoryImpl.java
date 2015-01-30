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
	@Named("UserMetaMapToModelConverter")
	private Function<Map<String, String>, User> UserMetaMapToModelConverter;

	public UserRepositoryImpl() {
		super("userName", User.class);
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public Set<User> getAll() throws Exception {
		return this.getBasicAll(UserMetaMapToModelConverter);
	}

	@Override
	public User getByName(final String userName) throws Exception {
		return this.getBasicById(userName, UserMetaMapToModelConverter);
	}

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
				found = UserMetaMapToModelConverter.apply(fieldValueMap);
				found.setNodeUri(nodeUri);
			}
		}
		return found;
	}

	@Override
	public void update(final User updateUser) throws Exception {
		this.updateBasic(updateUser);
	}

	@Override
	public void deleteByName(final String userName) throws Exception {
		this.deleteAllById(userName, true);
	}

	@Override
	public Page<User> paginateAll(final int pageOffset, final int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, UserMetaMapToModelConverter);
	}

}
