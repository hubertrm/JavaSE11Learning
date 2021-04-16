/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE :
 *        FILE : BusinessPolicies.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 16, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.policy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 16, 2021
 *
 * @author ARHS Developments - hubertrm
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BusinessPolicies {
	BusinessPolicy[] value();
}
