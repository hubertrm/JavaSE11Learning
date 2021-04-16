/* ----------------------------------------------------------------------------
 *     PROJECT : Java SE: Programming Complete - 25th Anniversary
 *
 *     PACKAGE : labs.client
 *        FILE : ResourceFormatter.java
 *
 *  CREATED BY : Hubert Romain
 *          ON : avr. 15, 2021
 *
 * ----------------------------------------------------------------------------
 */
package labs.client;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import labs.pm.data.Product;
import labs.pm.data.Review;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : avr. 15, 2021
 *
 * @author Hubert Romain - hubertrm
 */
public class ResourceFormatter {

	private Locale locale;
	private ResourceBundle resources;
	private DateTimeFormatter dateFormat;
	private NumberFormat moneyFormat;

	private static final Map<String, ResourceFormatter> formatters =
			Map.of("en-GB", new ResourceFormatter(Locale.UK),
					"en-US", new ResourceFormatter(Locale.US),
					"fr-FR", new ResourceFormatter(Locale.FRANCE));

	public static  ResourceFormatter getResourceFormatter(String languageTag) {
		return formatters.getOrDefault(languageTag, formatters.get("en-GB"));
	}

	public static Set<String> getSupportedLocales() {
		return formatters.keySet();
	}

	private ResourceFormatter(Locale locale) {
		this.locale = locale;
		resources = ResourceBundle.getBundle("resources", this.locale);
		dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(this.locale);
		moneyFormat = NumberFormat.getCurrencyInstance(this.locale);
	}

	public String formatProduct(Product product) {
		return this.formatData("product", product.getName(), moneyFormat.format(product.getPrice()),
				product.getRating().getStars(), dateFormat.format(product.getBestBefore()));
	}

	public String formatReview(Review review) {
		return this.formatData("review", review.getRating().getStars(), review.getComments());
	}

	public String formatProductReport(Product product, List<Review> reviews) {
		StringBuilder txt = new StringBuilder();
		txt.append(formatProduct(product)).append(System.lineSeparator());
		if (reviews.isEmpty()) {
			txt.append(formatData("no.reviews")).append(System.lineSeparator());
		} else {
			reviews.forEach(review -> txt.append(formatReview(review)));
		}
		return txt.toString();
	}

	public String getText(String key) {
		return resources.getString(key);
	}

	public String formatData(String resource, String ...values) {
		return MessageFormat.format(getText(resource), (Object[]) values);
	}
}
