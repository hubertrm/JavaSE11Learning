/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : Rating.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 01, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.data;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public enum Rating {

	NOT_RATED("NOT RATED"),
	ONE_STAR("\u2605\u2606\u2606\u2606\u2606"),
	TWO_STAR("\u2605\u2605\u2606\u2606\u2606"),
	THREE_STAR("\u2605\u2605\u2605\u2606\u2606"),
	FOUR_STAR("\u2605\u2605\u2605\u2605\u2606"),
	FIVE_STAR("\u2605\u2605\u2605\u2605\u2605");

	private final String starts;

	Rating(String rating) {
		this.starts = rating;
	}

	public String getStars() {
		return starts;
	}
}
