/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : Review.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 04, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.data;

import java.io.Serializable;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 04, 2021
 *
 * @author Hubert Romain - hubertrm
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
