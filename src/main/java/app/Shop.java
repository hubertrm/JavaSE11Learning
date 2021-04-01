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
		ProductManager productManager = new ProductManager();
		Product p1 = productManager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.THREE_STAR);
		Product p2 = productManager.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
		Product p3 = productManager.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		Product p4 = productManager.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now().plusDays(2));
		Product p5 = p3.applyRating(Rating.THREE_STAR);
		Product p6 = productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
		Product p7 = productManager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
		Product p8 = p4.applyRating(Rating.FIVE_STAR);
		Product p9 = p1.applyRating(Rating.TWO_STAR);
		System.out.println(p6.equals(p7));
		System.out.println(p1.getBestBefore());
		System.out.println(p3.getBestBefore());

		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
		System.out.println(p4);
		System.out.println(p5);
		System.out.println(p6);
		System.out.println(p7);
		System.out.println(p8);
		System.out.println(p9);
	}

}
