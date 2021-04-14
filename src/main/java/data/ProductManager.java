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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
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
import java.util.Objects;
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

	private Map<Product, List<Review>> products = new HashMap<>();
	private final ResourceBundle config = ResourceBundle.getBundle("config");
	private ResourceFormatter formatter;
	private static final Map<String, ResourceFormatter> formatters =
			Map.of("en-GB", new ResourceFormatter(Locale.UK),
					"en-US", new ResourceFormatter(Locale.US),
					"fr-FR", new ResourceFormatter(Locale.FRANCE));

	private final Path reportsFolder = Path.of(config.getString("reports.folder"));
	private final Path dataFolder = Path.of(config.getString("data.folder"));
	private final Path tempFolder = Path.of(config.getString("temp.folder"));

	private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
	private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

	public ProductManager(Locale locale) {
		this(locale.toLanguageTag());
	}

	public ProductManager(String languageTag) {
		changeLocale(languageTag);
		loadAllData();
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
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e, () -> "Error printing product report" + e.getMessage());
		}
	}

	public void printProductReport(Product product) throws IOException {
		List<Review> reviews = products.get(product);

		Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));
		try (PrintWriter out = new PrintWriter(
				new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE),
						StandardCharsets.UTF_8))) {
			out.append(formatter.formatProduct(product)).append(System.lineSeparator());
			Collections.sort(reviews);
			if (reviews.isEmpty()) {
				out.append(formatter.getText("no.reviews")).append(System.lineSeparator());
			} else {
				out.append(reviews.stream()
						.map(review -> formatter.formatReview(review) + System.lineSeparator())
						.collect(Collectors.joining()));
			}
		}
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

	private Review parseReview(String txt) {
		Review review = null;
		try {
			Object[] values = reviewFormat.parse(txt);
			review = new Review(Rateable.convert(Integer.parseInt((String)values[0])), (String)values[1]);
		} catch (ParseException | NumberFormatException e) {
			LOGGER.log(Level.WARNING, () -> "Error parsing review "+txt);
		}
		return review;
	}

	private Product parseProduct(String txt) {
		Product product = null;
		try {
			Object[] values = productFormat.parse(txt);
			int id = Integer.parseInt((String)values[1]);
			String name = (String)values[2];
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
			Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));
			switch ((String) values[0]) {
				case "D":
					product = new Drink(id, name, price, rating);
					break;
				case "F":
					LocalDate bestBefore = LocalDate.parse((String) values[5]);
					product = new Food(id, name, price, rating, bestBefore);
					break;
				default:
					break;
			}
		} catch (ParseException | NumberFormatException | DateTimeParseException e) {
			LOGGER.log(Level.WARNING, () -> "Error parsing product "+txt+" "+e.getMessage());
		}
		return product;
	}

	public Map<String, String> getDiscounts() {
		return products.keySet()
				.stream()
				.collect(Collectors.groupingBy(product -> product.getRating().getStars(),
						Collectors.collectingAndThen(Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
								formatter.moneyFormat::format)));
	}

	private void dumpData() {
		try {
			if (Files.notExists(tempFolder)) {
				Files.createDirectory(tempFolder);
			}
			Path tempFile = tempFolder.resolve(MessageFormat.format(config.getString("temp.file"), LocalDate.now()));
			try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
				out.writeObject(products);
				products = new HashMap<>();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, () -> "Error dumping data "+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void restoreData() {
		try {
			Path tempFile = Files.list(tempFolder)
										.filter(path -> path.getFileName().toString().endsWith("tmp"))
										.findFirst()
										.orElseThrow();
			try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
				products = (HashMap<Product, List<Review>>)in.readObject();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, () -> "Error restoring data "+e.getMessage());
		}
	}

	private void loadAllData() {
		try {
			products = Files.list(dataFolder)
					.filter(file -> file.getFileName().toString().startsWith("product"))
					.map(this::loadProduct)
					.filter(Objects::nonNull)
					.collect(Collectors.toMap(product -> product, this::loadReviews));
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, () -> "Error loading data "+e.getMessage());
		}
	}

	private Product loadProduct(Path file) {
		Product product = null;
		try {
			product = parseProduct(Files.lines(dataFolder.resolve(file), StandardCharsets.UTF_8).findFirst().orElseThrow());
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, () -> "Error loading product "+e.getMessage());
		}
		return product;
	}

	private List<Review> loadReviews(Product product) {
		List<Review> reviews = null;
		Path file = dataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
		if (Files.notExists(file)) {
			reviews = new ArrayList<>();
		} else {
			try {
				reviews = Files.lines(file, StandardCharsets.UTF_8)
						.map(this::parseReview)
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, () -> "Error loading reviews "+e.getMessage());
			}
		}
		return reviews;
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
