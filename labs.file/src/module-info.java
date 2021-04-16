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
module labs.file {
	requires java.logging;
	requires labs.pm;
	provides labs.pm.service.ProductManager with labs.file.service.ProductFileManager;
}
