package nz.co.pizzashack;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Set;

import nz.co.pizzashack.model.Pizzashack;
import nz.co.pizzashack.model.User;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class PizzashackDataInitSupport {
	private static final String delimiter = "||";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private final static String PIZZASHACK_INIT_FILE = "init/pizzashack-init.txt";
	private final static String USER_INIT_FILE = "init/user-init.txt";
	
	private Set<Pizzashack> pizzashackSet = Sets.<Pizzashack>newHashSet();
	private Set<User> userSet = Sets.<User>newHashSet();
	
	public PizzashackDataInitSupport() throws Exception {
		super();
		initPizzashacks();
	}

	private void initPizzashacks()throws Exception {
		File initPizzashackFile = new File(Resources.getResource(PIZZASHACK_INIT_FILE).getFile());
		try (BufferedReader reader = Files.newReader(initPizzashackFile, Charsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				Iterable<String> values = Splitter.on(delimiter).split(line);
				final Pizzashack pizzashack = new Pizzashack.Builder().pizzashackId(Iterables.get(values, 0)).pizzaName(Iterables.get(values, 1))
						.description(Iterables.get(values, 2)).price(new BigDecimal(Iterables.get(values, 3))).icon(Iterables.get(values, 4))
						.amount(Integer.valueOf(Iterables.get(values, 5)))
						.createTime(FORMAT.parse(Iterables.get(values, 6)))
						.viewed(Long.valueOf(Iterables.get(values, 7)))
						.build();
				
				pizzashackSet.add(pizzashack);
			}
		}
	}
	
	
	
}
