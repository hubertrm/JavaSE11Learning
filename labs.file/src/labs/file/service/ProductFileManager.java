/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : ProductManager.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 01, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.file.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import labs.pm.data.Drink;
import labs.pm.data.Food;
import labs.pm.data.Product;
import labs.pm.data.Rateable;
import labs.pm.data.Rating;
import labs.pm.data.Review;
import labs.pm.service.ProductManager;
import labs.pm.service.ProductManagerException;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public class ProductFileManager implements ProductManager {

	private static final Logger LOGGER = Logger.getLogger(ProductFileManager.class.getName());

	private Map<Product, List<Review>> products = new HashMap<>();
	private final ResourceBundle config = ResourceBundle.getBundle("labs.file.service.config");

	private final Path dataFolder = Path.of(config.getString("data.folder"));
	private final Path tempFolder = Path.of(config.getString("temp.folder"));

	private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
	private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock writeLock = lock.writeLock();
	private final Lock readLock = lock.readLock();

	public ProductFileManager() {
		loadAllData();
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product product = null;
		try {
			writeLock.lock();
			product = new Food(id, name, price, rating, bestBefore);
			products.putIfAbsent(product, new ArrayList<>());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e, () -> "Error adding product " + e.getMessage());
		} finally {
			writeLock.unlock();
		}
		return product;
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		Product product = null;
		try {
			writeLock.lock();
			product = new Drink(id, name, price, rating);
		products.putIfAbsent(product, new ArrayList<>());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e, () -> "Error adding product " + e.getMessage());
		} finally {
			writeLock.unlock();
		}
		return product;
	}

	public Product reviewProduct(int id, Rating rating, String comments) {
		try {
			writeLock.lock();
			return reviewProduct(findProduct(id), rating, comments);
		} catch (ProductManagerException e) {
			LOGGER.log(Level.INFO, e.getMessage());
			return null;
		} finally {
			writeLock.unlock();
		}
	}

	private Product reviewProduct(Product product, Rating rating, String comments) {
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

	public Product findProduct(int id) throws ProductManagerException {
		try {
			readLock.lock();
			return products.keySet()
					.stream()
					.filter(product -> product.getId() == id)
					.findFirst()
					.orElseThrow(() -> new ProductManagerException("Product with id "+id+" not found"));
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public List<Product> findProducts(Predicate<Product> filter) throws ProductManagerException {
		return products.keySet().stream().filter(filter).collect(Collectors.toList());
	}

	@Override
	public List<Review> findReviews(int id) throws ProductManagerException {
		return products.get(products.keySet()
				.stream()
				.filter(product -> product.getId() == id)
				.findFirst()
				.orElseThrow(() -> new ProductManagerException("Product with id "+id+" not found")));
	}

	@Override
	public Map<Rating, BigDecimal> getDiscounts(String languageTag) throws ProductManagerException {
		return products.keySet()
				.stream()
				.collect(Collectors.groupingBy(Product::getRating,
						Collectors.collectingAndThen(Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
								BigDecimal::valueOf)));
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

	private void dumpData() {
		try {
			if (Files.notExists(tempFolder)) {
				Files.createDirectory(tempFolder);
			}
			Path tempFile = tempFolder.resolve(MessageFormat.format(config.getString("temp.file"), LocalDate.now()));
			try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
				out.writeObject(products);
//				products = new HashMap<>();
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, () -> "Error dumping labs.pm.data "+e.getMessage());
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
}
