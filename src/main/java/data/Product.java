/* ----------------------------------------------------------------------------
 *     PROJECT : EURES
 *
 *     PACKAGE : data
 *        FILE : Product.java
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
package data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * {@code Product} class represents properties and behaviors of product objects
 * in the product Management System.
 * <br>
 *    Each product has an id, name, price
 * <br>
 * <p>ON : mars 21, 2021
 * Each product can have a discout, calculated based on a
 * {@link DISCOUNT_RATE discount rate}
 *
 * @version 1.0
 * @author ARHS Developments - hubertrm
 */
public abstract class Product {

	/**
	 * A constant that defines a {@link java.math.BigDecimal BigDecimal} value
	 * of the discount rate
	 * <br>
	 * Discount rate is 10%
	 */
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

	private int id;
	private String name;
	private BigDecimal price;
	private Rating rating;

	Product() {
		this(0, "no name", BigDecimal.ZERO);
	}

	Product(int id, String name, BigDecimal price) {
		this(id, name, price, Rating.NOT_RATED);
	}

	Product(int id, String name, BigDecimal price, Rating rating) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}

	public abstract Product applyRating(Rating newRating);

	/**
	 * Get the value of bestBefore
	 * @return the current date
	 */
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}

	/**
	 * Calculates discount based on a product price and
	 * {@link DISCOUNT_RATE discount rate}
	 * @return a {@link java.math.BigDecimal BigDecimal} value of the discount
	 */
	public BigDecimal getDiscount() {
		return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return id + ", " + name + ", " + price + ", " + getDiscount() + ", " + rating.getStars() + ", " + getBestBefore();
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 23 * hash + this.id;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Product){
			Product product = (Product) obj;
			return id == product.getId() && name.equals(product.getName()) && price.equals(product.getPrice()) &&
			       getDiscount().equals(product.getDiscount()) && rating.equals(product.getRating());
		}
		return false;
	}
}
