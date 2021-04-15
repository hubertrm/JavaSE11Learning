/* ----------------------------------------------------------------------------
 *     PROJECT : EURES
 *
 *     PACKAGE : PACKAGE_NAME
 *        FILE : app.Shop.java
 *
 *  CREATED BY : ARHS Developments
 *          ON : mars 21, 2021
 *
 * MODIFIED BY : ARHS Developments
 *          ON :
 *     VERSION :
 *
 * ----------------------------------------------------------------------------
 * Copyright (c) 2021 European Commission - DG EMPL
 * ----------------------------------------------------------------------------
 */
package app;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.Product;
import data.ProductManager;
import data.Rating;

/**
 * {@code Shop} class represents an application that manages Products
 * @version 1.0
 * <p>ON : mars 21, 2021
 * @author ARHS Developments - hubertrm
 */
public class Shop {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ProductManager productManager = ProductManager.getInstance();

		AtomicInteger clientCount = new AtomicInteger(0);
		Callable<String> client = () -> {
			String clientId = "Client " + clientCount.incrementAndGet();
			String threadName = Thread.currentThread().getName();
			int productId = ThreadLocalRandom.current().nextInt(63)+101;
			String languageTag = ProductManager.getSupportedLocales()
					.stream()
					.skip(ThreadLocalRandom.current().nextInt(3))
					.findFirst().get();
			StringBuilder log = new StringBuilder();
			log.append(clientId).append(" ").append(threadName).append("\n-\tstart of log\t-\n")
					.append(productManager.getDiscounts(languageTag)
			          .entrySet()
			          .stream()
			          .map(entry->entry.getKey()+"\t"+entry.getValue())
			          .collect(Collectors.joining("\n")));

			Product product = productManager.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
			log.append((product != null)
			           ? "\nProduct "+productId+" reviewed\n"
			           : "\nProduct "+productId+" not reviewed\n");
			productManager.printProductReport(productId, languageTag, clientId);
			log.append(clientId).append(" generated report for ").append(productId).append(" product")
					.append("\n-\tend of log\t-\n");
			return log.toString();
		};
		List<Callable<String>> clients = Stream.generate(() -> client).limit(5).collect(Collectors.toList());
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		try {
			List<Future<String>> results = executorService.invokeAll(clients);
			executorService.shutdown();
			results.forEach(result -> {
				try {
					System.out.println(result.get());
				} catch (InterruptedException | ExecutionException e) {
					Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, e,
							() -> "Error retrieving client log " + e.getMessage());
				}
			});
		} catch (InterruptedException e) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, e, () -> "Error invoking clients " + e.getMessage());
		}

//		productManager.printProductReport(101, "en-GB");
//		productManager.printProductReport(101, "fr-FR");
//		productManager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.THREE_STAR);
//		productManager.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
//		productManager.printProductReport(101);
//		productManager.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//		productManager.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//		productManager.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now().plusDays(2));
//		productManager.reviewProduct(103, Rating.THREE_STAR, "Nice");
//		productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
//		productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//		productManager.reviewProduct(104, Rating.FIVE_STAR, "Excellent");
//		productManager.printProductReport(104);
//		productManager.printProductReport(104);
//		productManager.reviewProduct(101, Rating.TWO_STAR, "Not sufficient");
//		productManager.getDiscounts().forEach((rating, discount) -> System.out.println(rating+"\t"+discount));
//		Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
//		Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
//		Predicate<Product> priceFilter = p -> p.getPrice().intValue() < 2;
//		productManager.printProducts(priceFilter, ratingSorter);
//		productManager.printProducts(priceFilter, priceSorter);
//		productManager.printProducts(priceFilter, priceSorter.thenComparing(ratingSorter).reversed());
	}

}
