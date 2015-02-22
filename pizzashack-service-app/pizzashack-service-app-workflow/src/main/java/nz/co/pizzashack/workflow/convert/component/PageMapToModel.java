package nz.co.pizzashack.workflow.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.Page;

import com.google.common.base.Function;

public class PageMapToModel<T> implements Function<Map<String, String>, Page<T>> {

	@Override
	public Page<T> apply(final Map<String, String> inputMap) {
		Page<T> page = null;
		if (inputMap != null) {
			page = new Page.Builder<T>()
					.pageSize(Integer.valueOf(inputMap.get("size")))
					.pageOffset(Integer.valueOf(inputMap.get("start")))
					.totalCount(Integer.valueOf(inputMap.get("total")))
					.build();
		}
		return page;
	}

}
