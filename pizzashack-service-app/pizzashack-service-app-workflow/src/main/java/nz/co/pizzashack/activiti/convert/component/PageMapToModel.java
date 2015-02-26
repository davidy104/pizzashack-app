package nz.co.pizzashack.activiti.convert.component;

import java.util.Map;

import nz.co.pizzashack.model.Page;

import com.google.common.base.Function;

public class PageMapToModel<T> implements Function<Map<String, Object>, Page<T>> {

	@Override
	public Page<T> apply(final Map<String, Object> inputMap) {
		Page<T> page = null;
		if (inputMap != null) {
			page = new Page.Builder<T>()
					.pageSize((Integer) inputMap.get("size"))
					.pageOffset((Integer) inputMap.get("start"))
					.totalCount((Integer) inputMap.get("total"))
					.build();
		}
		return page;
	}

}
