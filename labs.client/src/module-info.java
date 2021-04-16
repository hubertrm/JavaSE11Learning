/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE :
 *        FILE : module-info.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 15, 2021
 *
 * ----------------------------------------------------------------------------
 */
module labs.client {
	requires java.logging;
	requires labs.pm;
	requires labs.file;
	uses labs.pm.service.ProductManager;
}
