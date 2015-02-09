package nz.co.pizzashack.repository.convert;

import static nz.co.pizzashack.model.PizzashackCommentType.getPizzashackCommentType;
import static nz.co.pizzashack.util.GenericUtils.parseToDate;

import java.util.Map;

import nz.co.pizzashack.model.PizzashackComment;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;

public class PizzashackCommentMetaMapToModel implements Function<Map<String, String>, PizzashackComment> {

	@Override
	public PizzashackComment apply(final Map<String, String> inputMap) {
		PizzashackComment pizzashackComment = null;
		if (inputMap != null) {
			pizzashackComment = new PizzashackComment.Builder().message(inputMap.get("message")).build();
			if (!StringUtils.isEmpty(inputMap.get("commentType"))) {
				pizzashackComment.setCommentType(getPizzashackCommentType(inputMap.get("commentType")));
			}

			if (!StringUtils.isEmpty(inputMap.get("createTime"))) {
				pizzashackComment.setCreateTime(parseToDate("yyyy-MM-dd hh:mm:ss", inputMap.get("createTime")));
			}
		}
		return pizzashackComment;
	}

}
