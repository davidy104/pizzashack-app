package nz.co.pizzashack.test.convert;

import java.util.Date;

import nz.co.pizzashack.SharedModule;
import nz.co.pizzashack.config.ConfigurationServiceModule;
import nz.co.pizzashack.model.OrderReview;
import nz.co.pizzashack.model.ReviewStatus;
import nz.co.pizzashack.test.GuiceJUnitRunner;
import nz.co.pizzashack.test.GuiceJUnitRunner.GuiceModules;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules({ ConfigurationServiceModule.class, SharedModule.class })
public class ModelJsonConvertTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelJsonConvertTest.class);

	@Inject
	@Named("jacksonObjectMapper")
	private ObjectMapper jacksonObjectMapper;

	@Test
	public void testOrderReviewJsonConvert() throws Exception {
		OrderReview orderReview = new OrderReview.Builder().orderNo("O-2323iu2i")
				.content("the order amount is too big.")
				.createTime(new Date())
				.reviewStatus(ReviewStatus.REJECT)
				.nodeUri("http://localhost:7575/db/data/node/12345")
				.build();

		String json = jacksonObjectMapper.writeValueAsString(orderReview);
		LOGGER.info("json:{}", json);
	}

}
