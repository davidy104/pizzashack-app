package nz.co.pizzashack.repository;

import java.util.Map;

import nz.co.pizzashack.model.Customer;
import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.PizzashackComment;
import nz.co.pizzashack.model.User;
import nz.co.pizzashack.repository.convert.CustomerMetaMapToModel;
import nz.co.pizzashack.repository.convert.PizzashackCommentMetaMapToModel;
import nz.co.pizzashack.repository.convert.PizzashackMetaMapToModel;
import nz.co.pizzashack.repository.convert.PizzashackModelToCreateStatement;
import nz.co.pizzashack.repository.convert.PizzashackModelToMap;
import nz.co.pizzashack.repository.convert.UserMetaMapToModel;
import nz.co.pizzashack.repository.convert.UserModelToCreateStatement;
import nz.co.pizzashack.repository.convert.UserModelToMap;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class ConverterModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	@Named("pizzashackMetaMapToModelConverter")
	public Function<Map<String, String>, Pizzashack> pizzashackMetaMapToModelConverter() {
		return new PizzashackMetaMapToModel();
	}

	@Provides
	@Singleton
	@Named("pizzashackModelToCreateStatementConverter")
	public Function<Pizzashack, String> pizzashackModelToCreateStatementConverter() {
		return new PizzashackModelToCreateStatement();
	}

	@Provides
	@Singleton
	@Named("pizzashackModelToMapConverter")
	public Function<Pizzashack, Map<String, String>> pizzashackModelToMapConverter() {
		return new PizzashackModelToMap();
	}

	@Provides
	@Singleton
	@Named("pizzashackCommentMetaMapToModelConverter")
	public Function<Map<String, String>, PizzashackComment> pizzashackCommentMetaMapToModelConverter() {
		return new PizzashackCommentMetaMapToModel();
	}

	@Provides
	@Singleton
	@Named("userMetaMapToModelConverter")
	public Function<Map<String, String>, User> userMetaMapToModelConverter() {
		return new UserMetaMapToModel();
	}

	@Provides
	@Singleton
	@Named("userModelToMapConverter")
	public Function<User, Map<String, String>> userModelToMapConverter() {
		return new UserModelToMap();
	}

	@Provides
	@Singleton
	@Named("userModelToCreateStatementConverter")
	public Function<User, String> userModelToCreateStatementConverter() {
		return new UserModelToCreateStatement();
	}

	@Provides
	@Singleton
	@Named("customerMetaMapToModelConverter")
	public Function<Map<String, String>, Customer> customerMetaMapToModelConverter() {
		return new CustomerMetaMapToModel();
	}

}
