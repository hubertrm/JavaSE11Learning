/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : Drink.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 01, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public final class Drink extends Product {

	public Drink(int id, String name, BigDecimal price, Rating rating) {
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
