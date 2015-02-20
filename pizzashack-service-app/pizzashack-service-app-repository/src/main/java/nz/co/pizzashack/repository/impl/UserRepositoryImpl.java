package nz.co.pizzashack.repository.impl;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.RelationshipsLabel;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.UserRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class UserRepositoryImpl extends RepositoryBase<User, String> implements UserRepository {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(UserRepositoryImpl.class);

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("userMetaMapToModelConverter")
	private Function<Map<String, String>, User> userMetaMapToModelConverter;

	@Inject
	@Named("userModelToMapConverter")
	private Function<User, Map<String, String>> userModelToMapConverter;

	@Inject
	@Named("userModelToCreateStatementConverter")
	private Function<User, String> userModelToCreateStatementConverter;

	public UserRepositoryImpl() {
		super("User", "userName");
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

	@Override
	public String create(User addUser) throws Exception {
		return this.createUnique(addUser, userModelToCreateStatementConverter);
	}

	@Override
	public Set<User> getAll() throws Exception {
		return this.getBasicAll(userMetaMapToModelConverter);
	}

	@Override
	public User getByName(final String userName) throws NotFoundException {
		return this.getBasicById(userName, userMetaMapToModelConverter);
	}

	/**
	 * { "query" : "(u:User{userName:{userName},password:{password}}) RETURN u",
	 * "params" : { "password" : "{password}", "userName" : "{userName}" } }
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User getByNameAndPwd(final String userName, final String password) throws NotFoundException {
		User found = null;
		final String queryJson = "MATCH (u:User{userName:{userName},password:{password}}) RETURN u";
		AbstractCypherQueryResult result;
		try {
			result = this.getNeo4jRestAPIAccessor().cypherQuery(queryJson,
					Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
							.put("password", password)
							.put("userName", userName)
							.build()));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		if (result == null) {
			throw new NotFoundException("User not found by userName and password");
		}

		Map<String, Map<String, String>> metaMap = result.getNodeColumnMap().get("u");
		if (metaMap != null && !metaMap.isEmpty()) {
			final String nodeUri = (String) metaMap.keySet().toArray()[0];
			final Map<String, String> fieldValueMap = (Map<String, String>) metaMap.values().toArray()[0];
			found = userMetaMapToModelConverter.apply(fieldValueMap);
			found.setNodeUri(nodeUri);
		}
		return found;
	}

	@Override
	public void update(final User updateUser) throws Exception {
		this.updateBasicById(updateUser, userModelToMapConverter);
	}

	@Override
	public void deleteByName(final String userName) throws Exception {
		this.deleteAllById(userName, true);
	}

	@Override
	public Page<User> paginateAll(final int pageOffset, final int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, userMetaMapToModelConverter);
	}

	@Override
	public void gruntRole(final String userNodeUri, final String roleNodeUri, final Date createTime) throws Exception {
		neo4jRestAPIAccessor.buildRelationshipBetween2Nodes(userNodeUri, roleNodeUri, RelationshipsLabel.HasRole.name(),
				Maps.newHashMap(new ImmutableMap.Builder<String, String>()
						.put("createTime", formatDate("yyyy-MM-dd hh:mm:ss", createTime))
						.build()));
	}

	@Override
	public void revokeRole(final String userName, final String roleName) throws Exception {
		String queryJson = "OPTIONAL MATCH (u:User{userName:{userName}})-[r:" + RelationshipsLabel.HasRole.name() + "]->(:Role{roleName:{roleName}}) DELETE r";
		neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("roleName", roleName)
						.put("userName", userName)
						.build()));
	}

	@Override
	public Page<User> paginateByRole(final int pageOffset, final int pageSize, final String roleName) throws Exception {
		final String queryStatement = "OPTIONAL MATCH (u:User)-[:" + RelationshipsLabel.HasRole.name() + "]->(r:Role) WHERE r.roleName = '" + roleName + "' RETURN u";
		return this.doPagination(queryStatement, pageOffset, pageSize, userMetaMapToModelConverter);
	}

	@Override
	public Page<User> paginateByUserName(final int pageOffset, final int pageSize, final String userName) throws Exception {
		final String queryStatement = "OPTIONAL MATCH (u:User)-[:" + RelationshipsLabel.HasRole.name() + "]->(:Role) WHERE u.userName = '" + userName + "' RETURN u";
		return this.doPagination(queryStatement, pageOffset, pageSize, userMetaMapToModelConverter);
	}

}
