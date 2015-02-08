package nz.co.pizzashack.repository.impl;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.pizzashack.model.Page;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.RelationshipsLabel;
import nz.co.pizzashack.repository.PizzashackCommentRepository;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryNode;
import nz.co.pizzashack.repository.convert.template.AbstractCypherQueryResult;
import nz.co.pizzashack.repository.support.Neo4jRestAPIAccessor;
import nz.co.pizzashack.repository.support.RepositoryBase;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class PizzashackCommentRepositoryImpl extends RepositoryBase<PizzashackComment, String> implements PizzashackCommentRepository{

	@Inject
	private Neo4jRestAPIAccessor neo4jRestAPIAccessor;
	
	public PizzashackCommentRepositoryImpl() {
		super("PizzashackComment");
	}

	@Override
	public String createPizzashackComment(final String pizzashackNodeUri,final String userNodeUri, final PizzashackComment comment) throws Exception {
		final String commentNodeUri = this.create(comment, new Function<PizzashackComment,String>(){
			@Override
			public String apply(final PizzashackComment pizzashackComment) {
				if(pizzashackComment != null){
					List<String> fieldValueList = Lists.<String>newArrayList();
					fieldValueList.add("commentType:'"+pizzashackComment.getCommentType()+"'");
					fieldValueList.add("message:'"+pizzashackComment.getMessage()+"'");
					if(pizzashackComment.getCreateTime()!=null){
						fieldValueList.add("createTime:'"+formatDate("yyyy-MM-dd hh:mm:ss",pizzashackComment.getCreateTime())+"'");
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
		Set<PizzashackComment> resultSet = Collections.<PizzashackComment>emptySet();
		final String queryJson = "MATCH (p:Pizzashack{pizzashackId:{pizzashackId}}) OPTIONAL MATCH (p)-[:"+RelationshipsLabel.HasComment.name()+"]->(pc:PizzashackComment) RETURN DISTINCT pc";
		final AbstractCypherQueryResult result = this.getNeo4jRestAPIAccessor().cypherQuery(queryJson,
				Maps.newHashMap(new ImmutableMap.Builder<String, Object>()
						.put("pizzashackId", pizzashackId)
						.build()));
		
		if (result != null && !result.getDistinctNodes().isEmpty()) {
			resultSet = Sets.<PizzashackComment>newHashSet();
			for(final AbstractCypherQueryNode pcQueryNode : result.getDistinctNodes()){
				final String nodeUri = pcQueryNode.getUri();
				final Map<String,String> metaMap = pcQueryNode.getDataMap();
			}
		}
		return resultSet;
	}

	@Override
	public Set<PizzashackComment> getAllByUserName(final String userName) throws Exception {
		final String queryJson = "MATCH (u:User{userName:{userName}}) OPTIONAL MATCH (u)<-[:"+RelationshipsLabel.CommentBy.name()+"]-(pc:PizzashackComment) RETURN DISTINCT pc";
		return null;
	}

	@Override
	public Page<PizzashackComment> paginateByPizzashackIdAndUserName(final int pageOffset,final int pageSize,final String pizzashackId,final String userId) throws Exception {
		return null;
	}

	@Override
	public void deleteComment(final String userNodeUri,final String pizzashackNodeUri) throws Exception {
		
	}

	@Override
	protected Neo4jRestAPIAccessor getNeo4jRestAPIAccessor() {
		return neo4jRestAPIAccessor;
	}

}
