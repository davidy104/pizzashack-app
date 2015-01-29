package nz.co.pizzashack.repository.convert

import groovy.json.JsonOutput

import java.text.SimpleDateFormat

import nz.co.pizzashack.model.User

import com.google.common.base.Function

class UserModelToJson implements Function<User,String>{

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Override
	public String apply(final User user) {
		String createTime
		if(user.createTime){
			createTime = FORMAT.format(user.createTime)
		}
		return JsonOutput.toJson([userName: user.userName, password: user.password,createTime: createTime])
	}
}
