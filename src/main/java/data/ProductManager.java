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
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public class ProductManager {

	private static final Logger LOGGER = Logger.getLogger(ProductManager.class.getName());

	private final Map<Product, List<Review>> products = new HashMap<>();
	private final ResourceBundle config = ResourceBundle.getBundle("config");
	private ResourceFormatter formatter;
	private static final Map<String, ResourceFormatter> formatters =
			Map.of("en-GB", new ResourceFormatter(Locale.UK),
					"en-US", new ResourceFormatter(Locale.US),
					"fr-FR", new ResourceFormatter(Locale.FRANCE));

	private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
	private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

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
		try {
			return reviewProduct(findProduct(id), rating, comments);
		} catch (ProductManagerException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
		return null;
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
		try {
			printProductReport(findProduct(id));
		} catch (ProductManagerException e) {
			LOGGER.log(Level.INFO, e.getMessage());
		}
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
		LOGGER.log(Level.INFO, txt::toString);
	}

	public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
		StringBuilder txt = new StringBuilder();
		products.keySet()
				.stream()
				.sorted(sorter)
				.filter(filter)
				.forEach(product -> txt.append(formatter.formatProduct(product)).append('\n'));
		LOGGER.log(Level.INFO, txt::toString);
	}

	public Product findProduct(int id) throws ProductManagerException {
		return products.keySet()
				.stream()
				.filter(product -> product.getId() == id)
				.findFirst()
				.orElseThrow(() -> new ProductManagerException("Product with id "+id+" not found"));
	}

	public void changeLocale(String languageTag) {
		this.formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
	}

	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}

	public void parseReview(String txt) {
		try {
			Object[] values = reviewFormat.parse(txt);
			reviewProduct(Integer.parseInt((String)values[0]),
					Rateable.convert(Integer.parseInt((String)values[1])),
					(String)values[2]);
		} catch (ParseException | NumberFormatException e) {
			LOGGER.log(Level.WARNING, () -> "Error parsing review "+txt);
		}
	}

	public void parseProduct(String txt) {
		try {
			Object[] values = productFormat.parse(txt);
			int id = Integer.parseInt((String)values[1]);
			String name = (String)values[2];
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
			Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));
			switch ((String) values[0]) {
			case "D":
				createProduct(id, name, price, rating);
			case "F":
				LocalDate bestBefore = LocalDate.parse((String) values[5]);
				createProduct(id, name, price, rating, bestBefore);
			}
		} catch (ParseException | NumberFormatException | DateTimeParseException e) {
			LOGGER.log(Level.WARNING, () -> "Error parsing product "+txt);
		}
	}

	public Map<String, String> getDiscounts() {
		return products.keySet()
				.stream()
				.collect(Collectors.groupingBy(product -> product.getRating().getStars(),
						Collectors.collectingAndThen(Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
								formatter.moneyFormat::format)));
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
