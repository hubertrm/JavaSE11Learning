/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : Food.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 01, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public final class Food extends Product {

	private LocalDate bestBefore;

	public Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;

	}

	/**
	 * Get the value of bestBefore
	 * @return the value of bestBefore
	 */
	public LocalDate getBestBefore() {
		return bestBefore;
	}

	@Override
	public Product applyRating(Rating newRating) {
		return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
	}

	@Override
	public BigDecimal getDiscount() {
		return (bestBefore.isEqual(LocalDate.now())) ? super.getDiscount() : BigDecimal.ZERO;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + bestBefore;
	}
}
