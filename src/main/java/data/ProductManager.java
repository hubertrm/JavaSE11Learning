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
import java.time.LocalDate;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public class ProductManager {

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		return new Food(id, name, price, rating, bestBefore);
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		return new Drink(id, name, price, rating);
	}

}
