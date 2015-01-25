package nz.co.pizzashack.test;

import java.io.BufferedReader;
import java.io.File;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import nz.co.pizzashack.model.Pizzashack;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

public class TestUtils {
	private static final String delimiter = "||";

	public static Set<Pizzashack> initPizzashackFromFile(final String clazPathFile){
		Set<Pizzashack> pizzashackSet = Sets.<Pizzashack>newHashSet();
		
		try {
			File initPizzashackFile = new File(Resources.getResource(clazPathFile).getFile());
			try (BufferedReader reader = Files.newReader(initPizzashackFile, Charsets.UTF_8)) {
				String line;
				while ((line = reader.readLine()) != null) {
					Iterable<String> values = Splitter.on(delimiter).split(line);
					final Pizzashack pizzashack = new Pizzashack.Builder().pizzashackId(UUID.randomUUID().toString()).pizzaName(Iterables.get(values, 0))
							.description(Iterables.get(values, 1)).price(new BigDecimal(Iterables.get(values, 2))).build();
					System.out.println("pizzashack:{} "+pizzashack);
					pizzashackSet.add(pizzashack);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} 
		return pizzashackSet;
	}
}
