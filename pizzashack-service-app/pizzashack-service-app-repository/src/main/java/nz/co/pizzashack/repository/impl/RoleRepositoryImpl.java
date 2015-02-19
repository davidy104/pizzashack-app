package nz.co.pizzashack.repository.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.RelationshipsLabel;
import nz.co.pizzashack.model.Role;
import nz.co.pizzashack.repository.RoleRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RoleRepositoryImpl extends RepositoryBase<Role, String> implements RoleRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);

	public RoleRepositoryImpl() {
		super("Role", "roleName");
	}

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	@Inject
	@Named("roleMetaMapToModelConverter")
	private Function<Map<String, String>, Role> roleMetaMapToModelConverter;

	@Inject
	@Named("roleModelToCreateStatementConverter")
	private Function<Role, String> roleModelToCreateStatementConverter;

	@Inject
	@Named("roleModelToMapConverter")
	private Function<Role, Map<String, String>> roleModelToMapConverter;

	@Override
	public Set<Role> getByUserName(final String userName) {
		Set<Role> resultSet = Collections.<Role> emptySet();
		final String query = "MATCH (u:User{userName:{userName}}) OPTIONAL MATCH (u)-[:" + RelationshipsLabel.HasRole.name() + "]->(role:Role) RETURN DISTINCT role";
		LOGGER.info("query:{} ", query);
		AbstractCypherQueryResult result = null;
		try {
			result = neo4jRestAPIAccessor.cypherQuery(query,
					Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
							.put("userName", userName)
							.build()));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		if (result != null) {
			resultSet = Sets.<Role> newHashSet();
			Set<AbstractCypherQueryNode> metaNodes = result.getDistinctNodes();
			for (final AbstractCypherQueryNode metaNode : metaNodes) {
				final String nodeUri = metaNode.getUri();
				final Map<String, String> metaMap = metaNode.getDataMap();
				Role role = roleMetaMapToModelConverter.apply(metaMap);
				role.setNodeUri(nodeUri);
				resultSet.add(role);
			}
		}
		return resultSet;
	}

	@Override
	public String create(final Role addRole) throws Exception {
		return this.createUnique(addRole, roleModelToCreateStatementConverter);
	}

	@Override
	public void update(final Role updateRole) throws Exception {
		this.updateBasicById(updateRole, roleModelToMapConverter);
	}

	@Override
	public void deleteByName(final String roleName) throws Exception {
		this.deleteAllById(roleName, true);
	}

	@Override
	public Set<Role> getAll() throws Exception {
		return this.getBasicAll(roleMetaMapToModelConverter);
	}

	@Override
	public Role getByName(final String roleName) throws NotFoundException {
		return this.getBasicById(roleName, roleMetaMapToModelConverter);
	}

	@Override
	public Page<Role> paginateAll(int pageOffset, int pageSize) throws Exception {
		return this.paginationBasic(pageOffset, pageSize, roleMetaMapToModelConverter);
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

}
