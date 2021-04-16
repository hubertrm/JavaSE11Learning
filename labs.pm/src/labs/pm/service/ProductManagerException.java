/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : ProductManagerException.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 11, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.service;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 11, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public class ProductManagerException extends Exception {

	public ProductManagerException() {
		super();
	}

	public ProductManagerException(String message) {
		super(message);
	}

	public ProductManagerException(String message, Throwable cause) {
		super(message, cause);
	}

}
