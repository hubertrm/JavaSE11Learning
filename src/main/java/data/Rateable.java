/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : Rateable.java
 *
 *  CREATED BY : ARHS Developments
 *          ON : avr. 04, 2021
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

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 04, 2021
 *
 * @author ARHS Developments - hubertrm
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
