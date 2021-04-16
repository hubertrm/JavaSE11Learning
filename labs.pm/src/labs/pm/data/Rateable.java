/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : Rateable.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 04, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.data;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 04, 2021
 *
 * @author Hubert Romain - hubertrm
 */
@FunctionalInterface
public interface Rateable<T> {
	public static final Rating DEFAULT_RATING = Rating.NOT_RATED;

	public abstract T applyRating(Rating rating);

	public default T applyRating(int stars) {
		return applyRating(convert(stars));
	}

	public default Rating getRating() {
		return DEFAULT_RATING;
	}

	public static Rating convert(int stars) {
		return (stars >= 0 && stars <= 5) ? Rating.values()[stars] : DEFAULT_RATING;
	}

}
