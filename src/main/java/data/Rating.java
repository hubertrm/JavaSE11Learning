/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : Rating.java
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

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public enum Rating {

	NOT_RATED("NOT RATED"),
	ONE_STAR("★☆☆☆☆"),
	TWO_STAR("★★☆☆☆"),
	THREE_STAR("★★★☆☆"),
	FOUR_STAR("★★★★☆"),
	FIVE_STAR("★★★★★");

	private final String starts;

	Rating(String rating) {
		this.starts = rating;
	}

	public String getStars() {
		return starts;
	}
}
