/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.jar.main
 *        FILE : Main.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 16, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.jar.main;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 16, 2021
 *
 * @author ARHS Developments - hubertrm
 */
public class Main {
	public static void main(String[] args) {
		System.out.println("Main from Java 11 (jar)");
		if (args.length > 0) {
			for (String arg : args) {
				System.out.println(" Arg: '" + arg + "'");
			}
		}
	}
}
