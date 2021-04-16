/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.pm.data
 *        FILE : ProductManager.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 01, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.pm.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import labs.pm.data.Product;
import labs.pm.data.Rating;
import labs.pm.data.Review;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 01, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public interface ProductManager {

	Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) throws ProductManagerException;

	Product createProduct(int id, String name, BigDecimal price, Rating rating) throws ProductManagerException;

	Product reviewProduct(int id, Rating rating, String comments) throws ProductManagerException;

	Product findProduct(int id) throws ProductManagerException;

	List<Product> findProducts(Predicate<Product> filter) throws ProductManagerException;

	List<Review> findReviews(int id) throws ProductManagerException;

	Map<Rating, BigDecimal> getDiscounts(String languageTag) throws ProductManagerException;
}
