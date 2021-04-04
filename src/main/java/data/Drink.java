/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : Drink.java
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
import java.time.LocalTime;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public final class Drink extends Product {

	Drink(int id, String name, BigDecimal price, Rating rating) {
		super(id, name, price, rating);
	}

	@Override
	public Product applyRating(Rating newRating) {
		return new Drink(getId(), getName(), getPrice(), newRating);
	}

	@Override
	public BigDecimal getDiscount() {
		LocalTime now = LocalTime.now();
		return (now.isAfter(LocalTime.of(17, 30)) && now.isBefore(LocalTime.of(18,30)))
				? super.getDiscount() : BigDecimal.ZERO;
	}
}
