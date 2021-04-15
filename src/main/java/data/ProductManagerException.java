/* ----------------------------------------------------------------------------
 *     PROJECT : EUROPASS
 *
 *     PACKAGE : data
 *        FILE : ProductManagerException.java
 *
 *  CREATED BY : ARHS Developments
 *          ON : avr. 11, 2021
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
 * <p>ON : avr. 11, 2021
 *
 * @author ARHS Developments - hubertrm
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
