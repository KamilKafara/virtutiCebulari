package pl.promotion.finder.feature.product.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class PriceMapper {
    private static String currency = " zł";
    private static BigDecimal decimalPrice;

    private PriceMapper() {
    }

    public static BigDecimal getDecimalPrice() {
        return decimalPrice;
    }

    public static synchronized void setCurrency(String currency) {
        PriceMapper.currency = currency;
    }

    public static String priceFactory(String context) throws ParseException {
        if (context == null) {
            return "0.00" + currency;
        }
        context = removeWhitespaces(context);

        int comaArrayLength = context.split(",").length;
        int dotArrayLength = context.split("\\.").length;

        if (comaArrayLength == 2 && dotArrayLength == 2) {
            context = removeFirstSeparator(context);
        }

        if (comaArrayLength == 3 || dotArrayLength == 3) {
            context = removeFirstSeparator(context);
        }

        String clearContext = clear(context);
        BigDecimal bigDecimal = parse(clearContext, Locale.US).setScale(2);
        PriceMapper.decimalPrice = bigDecimal;

        return addCurrency(bigDecimal.toString());
    }

    private static String removeFirstSeparator(String context) {
        String newContext = context.replace(",", "\\.");
        return newContext.replaceFirst("\\.", "");
    }

    private static String clear(String price) {
        return removeWhitespaces(price.replace(",", "."));
    }

    private static String removeWhitespaces(String context) {
        return context.replaceAll("\\s", "");
    }

    private static String addCurrency(String context) {
        return context + PriceMapper.currency;
    }

    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
    }
}
