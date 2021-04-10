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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

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
		ProductManager productManager = new ProductManager("en-GB");
		productManager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.THREE_STAR);
//		productManager.printProductReport(101);
//		productManager.changeLocale("fr-FR");
		productManager.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
//		productManager.printProductReport(101);
		productManager.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//		productManager.printProductReport(102);
		productManager.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		productManager.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now().plusDays(2));
//		productManager.printProductReport(105);
		productManager.reviewProduct(103, Rating.THREE_STAR, "Nice");
//		productManager.printProductReport(103);
		productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		productManager.reviewProduct(104, Rating.FIVE_STAR, "Excellent");

//		productManager.printProductReport(104);
		productManager.reviewProduct(101, Rating.TWO_STAR, "Not sufficient");
//		productManager.printProductReport(101);
		Comparator<Product> ratingSorter = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
		Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
		productManager.printProducts(ratingSorter);
		productManager.printProducts(priceSorter);
		productManager.printProducts(priceSorter.thenComparing(ratingSorter));
	}

}
