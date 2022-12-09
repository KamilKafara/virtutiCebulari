package pl.promotion.finder.utils;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Log4j2
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

    public static BigDecimal priceFactory(String context) {
        if (context == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.UP);
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
        BigDecimal bigDecimal = null;
        try {
            bigDecimal = parse(clearContext, Locale.US).setScale(2, RoundingMode.UP);
        } catch (ParseException e) {
            log.warn(e.getMessage());
            return BigDecimal.ZERO.setScale(2, RoundingMode.UP);
        }
        PriceMapper.decimalPrice = bigDecimal;
        return bigDecimal;
    }

    public static String priceFactoryWithCurrency(BigDecimal context) {
        return addCurrency(priceFactory(context.toString()).toString());
    }

    public static String priceFactoryWithCurrency(String context) {
        return addCurrency(priceFactory(context).toString());
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
