/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : ProductManager.java
 *
 *  CREATED BY : ARHS Developments
 *          ON : avr. 01, 2021
 *
 * MODIFIED BY : ARHS Developments
 *          ON :
 *     VERSION :
 *
 * ----------------------------------------------------------------------------
 * Copyright (c) 2021 European Commission - DG EMPL
 * ----------------------------------------------------------------------------
 */
package data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public class ProductManager {

	private final Map<Product, List<Review>> products = new HashMap<>();
	private ResourceFormatter formatter;
	private static final Map<String, ResourceFormatter> formatters =
			Map.of("en-GB", new ResourceFormatter(Locale.UK),
					"en-US", new ResourceFormatter(Locale.US),
					"fr-FR", new ResourceFormatter(Locale.FRANCE));

	public ProductManager(Locale locale) {
		this(locale.toLanguageTag());
	}

	public ProductManager(String languageTag) {
		changeLocale(languageTag);
	}


	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product product = new Food(id, name, price, rating, bestBefore);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		Product product = new Drink(id, name, price, rating);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}

	public Product reviewProduct(int id, Rating rating, String comments) {
		return reviewProduct(findProduct(id), rating, comments);
	}

	public Product reviewProduct(Product product, Rating rating, String comments) {
		List<Review> reviews = products.get(product);
		products.remove(product, reviews);
		reviews.add(new Review(rating, comments));

		product = product.applyRating(Rateable.convert((int)Math.round(reviews
				.stream()
				.map(review -> review.getRating().ordinal())
				.mapToInt(Integer::intValue)
				.average()
				.orElse(0))));
		products.put(product, reviews);
		return product;
	}

	public void printProductReport(int id) {
		printProductReport(findProduct(id));
	}

	public void printProductReport(Product product) {
		List<Review> reviews = products.get(product);
		StringBuilder txt = new StringBuilder();
		txt.append(formatter.formatProduct(product));
		txt.append('\n');
		Collections.sort(reviews);
		if (reviews.isEmpty()) {
			txt.append(formatter.getText("no.reviews")).append('\n');
		} else {
			txt.append(reviews.stream()
			          .map(review -> formatter.formatReview(review) + '\n')
			          .collect(Collectors.joining()));
		}
		System.out.println(txt);
	}

	public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
		StringBuilder txt = new StringBuilder();
		products.keySet()
				.stream()
				.sorted(sorter)
				.filter(filter)
				.forEach(product -> txt.append(formatter.formatProduct(product) + '\n'));
		System.out.println(txt);
	}

	public Product findProduct(int id) {
		return products.keySet()
				.stream()
				.filter(product -> product.getId() == id)
				.findFirst()
				.orElseGet(() -> null);
	}

	public void changeLocale(String languageTag) {
		this.formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
	}

	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}

	private static class ResourceFormatter {

		private final Locale locale;
		private final ResourceBundle resources;
		private final DateTimeFormatter dateFormat;
		private final NumberFormat moneyFormat;

		private ResourceFormatter(Locale locale) {
			this.locale = locale;
			resources = ResourceBundle.getBundle("resources", this.locale);
			dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(this.locale);
			moneyFormat = NumberFormat.getCurrencyInstance(this.locale);
		}

		private String formatProduct(Product product) {
			return MessageFormat.format(resources.getString("product"),
					product.getName(),
					moneyFormat.format(product.getPrice()),
					product.getRating().getStars(),
					dateFormat.format(product.getBestBefore()));
		}

		private String formatReview(Review review) {
			return MessageFormat.format(resources.getString("review"),
					review.getRating().getStars(),
					review.getComments());
		}

		private String getText(String key) {
			return resources.getString(key);
		}
	}
}
