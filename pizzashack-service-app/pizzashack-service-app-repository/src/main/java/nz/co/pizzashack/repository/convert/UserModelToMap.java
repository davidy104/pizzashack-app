package nz.co.pizzashack.repository.convert;

import java.util.Map;

import nz.co.pizzashack.model.User;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import static nz.co.pizzashack.util.GenericUtils.formatDate;

public class UserModelToMap implements Function<User,Map<String,String>>{

	@Override
	public Map<String, String> apply(final User user) {
		Map<String,String> resultMap = Maps.<String,String>newHashMap();
		if(user != null){
			resultMap.put("userName", user.getUserName());
			resultMap.put("password", user.getPassword());
			if(user.getCreateTime()!=null){
				resultMap.put("createTime", formatDate("yyyy-MM-dd hh:mm:ss",user.getCreateTime()));
			}
		}
		return resultMap;
	}

}
