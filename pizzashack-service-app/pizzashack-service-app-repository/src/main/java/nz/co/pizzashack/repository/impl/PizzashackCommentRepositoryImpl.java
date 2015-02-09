package nz.co.pizzashack.repository.impl;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.NotFoundException;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.RelationshipsLabel;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PizzashackCommentRepositoryImpl extends RepositoryBase<PizzashackComment, String> implements PizzashackCommentRepository {

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;

	private static final Logger LOGGER = LoggerFactory.getLogger(PizzashackCommentRepositoryImpl.class);

	@Inject
	@Named("pizzashackCommentMetaMapToModelConverter")
	private Function<Map<String, String>, PizzashackComment> pizzashackCommentMetaMapToModelConverter;

	public PizzashackCommentRepositoryImpl() {
		super("PizzashackComment");
	}

	@Override
	public String createPizzashackComment(final String pizzashackNodeUri, final String userNodeUri, final PizzashackComment comment) throws Exception {
		final String commentNodeUri = this.create(comment, new Function<PizzashackComment, String>() {
			@Override
			public String apply(final PizzashackComment pizzashackComment) {
				if (pizzashackComment != null) {
					List<String> fieldValueList = Lists.<String> newArrayList();
					fieldValueList.add("commentType:'" + pizzashackComment.getCommentType() + "'");
					fieldValueList.add("message:'" + pizzashackComment.getMessage() + "'");
					if (pizzashackComment.getCreateTime() != null) {
						fieldValueList.add("createTime:'" + formatDate("yyyy-MM-dd hh:mm:ss", pizzashackComment.getCreateTime()) + "'");
					}
					return Joiner.on(",").join(fieldValueList);
				}
				return null;
			}
		});
		neo4jRestAPIAccessor.buildRelationshipBetween2Nodes(commentNodeUri, userNodeUri, RelationshipsLabel.CommentBy.name());
		neo4jRestAPIAccessor.buildRelationshipBetween2Nodes(pizzashackNodeUri, commentNodeUri, RelationshipsLabel.HasComment.name());
		return commentNodeUri;
	}

	@Override
	public Set<PizzashackComment> getAllByPizzashackId(final String pizzashackId) throws Exception {
		return this.doQueryResultMapToModelsConvert(this.doGetByPizzashackId(pizzashackId));
	}

	@Override
	public Set<PizzashackComment> getAllByUserName(final String userName) throws Exception {
		return this.doQueryResultMapToModelsConvert(this.doGetByUserName(userName));
	}

	@Override
	public PizzashackComment getByPizzashackIdAndUserName(final String pizzashackId, final String userName) throws Exception {
		final String query = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) "
				+ " OPTIONAL MATCH (p)-[:" + RelationshipsLabel.HasComment.name() + "]->(pc:PizzashackComment)"
				+ "-[:" + RelationshipsLabel.CommentBy.name() + "]->(u:User{userName:{userName}}) RETURN DISTINCT pc";
		LOGGER.info("getByPizzashackIdAndUserName:{} ", query);

		final AbstractCypherQueryResult result = neo4jRestAPIAccessor.cypherQuery(query,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.put("userName", userName)
						.build()));
		if (result == null) {
			throw new NotFoundException("Comments not found by id[" + pizzashackId + "] and userName[" + userName + "]");
		}
		Set<PizzashackComment> comments = this.doQueryResultMapToModelsConvert(result);
		if (!comments.isEmpty()) {
			return (PizzashackComment) comments.toArray()[0];
		}
		return null;
	}

	private Set<PizzashackComment> doQueryResultMapToModelsConvert(final AbstractCypherQueryResult result) {
		Set<PizzashackComment> resultSet = Collections.<PizzashackComment> emptySet();
		if (result != null && !result.getDistinctNodes().isEmpty()) {
			resultSet = Sets.<PizzashackComment> newHashSet();
			for (final AbstractCypherQueryNode pcQueryNode : result.getDistinctNodes()) {
				final String nodeUri = pcQueryNode.getUri();
				final Map<String, String> metaMap = pcQueryNode.getDataMap();
				PizzashackComment pizzashackComment = pizzashackCommentMetaMapToModelConverter.apply(metaMap);
				pizzashackComment.setNodeUri(nodeUri);
				resultSet.add(pizzashackComment);
			}
		}
		return resultSet;
	}

	@Override
	public void deleteCommentByUserName(final String userName) throws Exception {
		final String queryJson = "MATCH (u:User{userName:{userName}}) OPTIONAL MATCH (u)<-[c:" + RelationshipsLabel.CommentBy.name() + "]-(pc:PizzashackComment)"
				+ "<-[hc:" + RelationshipsLabel.HasComment.name() + "]-(p:Pizzashack)"
				+ " DELETE pc, c , hc";
		neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("userName", userName)
						.build()));
	}

	@Override
	public void deleteCommentByPizzashackId(final String pizzashackId) throws Exception {
		final String queryJson = "OPTIONAL MATCH (u:User)<-[c:" + RelationshipsLabel.CommentBy.name() + "]-(pc:PizzashackComment)"
				+ "<-[hc:" + RelationshipsLabel.HasComment.name() + "]-(p:Pizzashack{pizzashackId:{pizzashackId}})"
				+ " DELETE pc, c , hc";
		neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.build()));
	}

	private AbstractCypherQueryResult doGetByPizzashackId(final String pizzashackId) throws Exception {
		final String queryJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) OPTIONAL MATCH (p)-[r:" + RelationshipsLabel.HasComment.name() + "]->(pc:PizzashackComment) RETURN DISTINCT pc,r,p";
		return neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.build()));
	}

	private AbstractCypherQueryResult doGetByUserName(final String userName) throws Exception {
		final String queryJson = "MATCH (u:User{userName:{userName}}) OPTIONAL MATCH (u)<-[r:" + RelationshipsLabel.CommentBy.name() + "]-(pc:PizzashackComment) RETURN DISTINCT pc,r,u";
		return neo4jRestAPIAccessor.cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("userName", userName)
						.build()));
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

}
