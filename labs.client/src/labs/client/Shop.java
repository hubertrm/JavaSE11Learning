/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : PACKAGE_NAME
 *        FILE : labs.client.Shop.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : mars 21, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.client;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import labs.pm.data.Product;
import labs.pm.data.Rating;
import labs.pm.data.Review;
import labs.pm.service.ProductManager;
import labs.pm.service.ProductManagerException;

/**
 * {@code Shop} class represents an application that manages Products
 * @version 1.0
 * <p>ON : mars 21, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public class Shop {

	private static  final Logger logger = Logger.getLogger(Shop.class.getName());

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			ResourceFormatter formatter = ResourceFormatter.getResourceFormatter("en-GB");
			ServiceLoader<ProductManager> serviceLoader = ServiceLoader.load(ProductManager.class);
			ProductManager productManager = serviceLoader.findFirst().orElseThrow(ProductManagerException::new);

			productManager.createProduct(164, "Kombucha", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
			productManager.reviewProduct(164, Rating.TWO_STAR, "Looks like tea but is it?");
			productManager.reviewProduct(164, Rating.THREE_STAR, "Fine Tea?");
			productManager.reviewProduct(164, Rating.FOUR_STAR, "This is not tea");
			productManager.reviewProduct(164, Rating.FIVE_STAR, "Perfect");

			productManager.findProducts(product -> product.getPrice().doubleValue() < 2)
					.forEach(product -> System.out.println(formatter.formatProduct(product)));
			Product product = productManager.findProduct(101);
			List<Review> reviews = productManager.findReviews(101);

			System.out.println(formatter.formatProduct(product));
			reviews.forEach(review -> System.out.println(formatter.formatReview(review)));
			printFile(formatter.formatProductReport(product, reviews), Path.of(formatter.formatData("report",
					String.valueOf(product.getId()))));
		} catch (ProductManagerException e) {
			logger.log(Level.SEVERE, e, () -> "Error invoking clients " + e.getMessage());
		}
	}

	public static void printFile(String content, Path file) {
		try {
			Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e, () -> "Error writing to file " + e.getMessage());
		}
	}

}
