package nz.co.pizzashack;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.util.Set;

import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class PizzashackInitUtils {
	
	private static final String delimiter = "||";
	
	public static Set<Pizzashack> initPizzashackFromFile(final String clazPathFile) throws Exception {
		Set<Pizzashack> pizzashackSet = Sets.<Pizzashack>newHashSet();
		File initPizzashackFile = new File(Resources.getResource(clazPathFile).getFile());
			try (BufferedReader reader = Files.newReader(initPizzashackFile, Charsets.UTF_8)) {
				String line;
				while ((line = reader.readLine()) != null) {
					Iterable<String> values = Splitter.on(delimiter).split(line);
					final Pizzashack pizzashack = new Pizzashack.Builder().pizzashackId(Iterables.get(values, 0)).pizzaName(Iterables.get(values, 1))
							.description(Iterables.get(values, 2)).price(new BigDecimal(Iterables.get(values, 3))).build();
					System.out.println("pizzashack:{} "+pizzashack);
					pizzashackSet.add(pizzashack);
				}
			}
		return pizzashackSet;
	}
}
