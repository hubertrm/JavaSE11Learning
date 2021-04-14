/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : Review.java
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

import java.io.Serializable;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 04, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public class Review implements Comparable<Review>, Serializable {

	private Rating rating;
	private String comments;

	public Review(Rating rating, String comments) {
		this.rating = rating;
		this.comments = comments;
	}

	public Rating getRating() {
		return rating;
	}

	public String getComments() {
		return comments;
	}

	@Override
	public String toString() {
		return "Review{" + "rating=" + rating + ", comments='" + comments + '\'' + '}';
	}

	@Override
	public int compareTo(Review other) {
		return other.rating.ordinal() - this.rating.ordinal();
	}
}
