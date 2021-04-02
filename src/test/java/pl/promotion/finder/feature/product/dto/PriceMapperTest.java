package pl.promotion.finder.feature.product.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PriceMapperTest {

    private final List<String> prices = Arrays.asList(
            "12345.67",
            "12345.67zł",
            "12345.67 zł",
            "12345.67PLN",
            "12345,67zł",
            "12345,67 zł",
            "12345,67PLN",
            "1 2345.67zł",
            "1 2345.67 zł",
            "1 2345.67PLN",
            "1 2345,67zł",
            "1 2345,67 zł",
            "1 2345,67PLN",
            "1 2345zł",
            "1 2345 zł",
            "1 2345PLN",
            "   1 2345PLN   "
    );

    private final List<String> nullPrices = Arrays.asList(
            null,
            "0.00 zł",
            "0.00zł",
            "0,00 PLN",
            "0,00PLN",
            "   0,00PLN  ",
            "000",
            ""
    );

    @Test
    public void testFullPrices() throws ParseException {
        String currency = " PLN";
        String expectedPrice = "12345.67" + currency;

        PriceMapper.setCurrency(currency);

        String result0 = PriceMapper.priceFactory(prices.get(0));
        String result1 = PriceMapper.priceFactory(prices.get(1));
        String result2 = PriceMapper.priceFactory(prices.get(2));
        String result3 = PriceMapper.priceFactory(prices.get(3));
        String result4 = PriceMapper.priceFactory(prices.get(4));
        String result5 = PriceMapper.priceFactory(prices.get(5));
        String result6 = PriceMapper.priceFactory(prices.get(6));
        String result7 = PriceMapper.priceFactory(prices.get(7));
        String result8 = PriceMapper.priceFactory(prices.get(8));
        String result9 = PriceMapper.priceFactory(prices.get(9));
        String result10 = PriceMapper.priceFactory(prices.get(10));
        String result11 = PriceMapper.priceFactory(prices.get(11));
        String result12 = PriceMapper.priceFactory(prices.get(12));

        assertEquals(expectedPrice, result0);
        assertEquals(expectedPrice, result1);
        assertEquals(expectedPrice, result2);
        assertEquals(expectedPrice, result3);
        assertEquals(expectedPrice, result4);
        assertEquals(expectedPrice, result5);
        assertEquals(expectedPrice, result6);
        assertEquals(expectedPrice, result7);
        assertEquals(expectedPrice, result8);
        assertEquals(expectedPrice, result9);
        assertEquals(expectedPrice, result10);
        assertEquals(expectedPrice, result11);
        assertEquals(expectedPrice, result12);
    }

    @Test
    public void testNullPrices() throws ParseException {
        String currency = " PLN";
        String expectedPriceWithNullRest = "0.00" + currency;

        PriceMapper.setCurrency(currency);

        String result0 = PriceMapper.priceFactory(nullPrices.get(0));
        String result1 = PriceMapper.priceFactory(nullPrices.get(1));
        String result2 = PriceMapper.priceFactory(nullPrices.get(2));
        String result3 = PriceMapper.priceFactory(nullPrices.get(3));
        String result4 = PriceMapper.priceFactory(nullPrices.get(4));
        String result5 = PriceMapper.priceFactory(nullPrices.get(5));
        String result6 = PriceMapper.priceFactory(nullPrices.get(6));

        assertEquals(expectedPriceWithNullRest, result0);
        assertEquals(expectedPriceWithNullRest, result1);
        assertEquals(expectedPriceWithNullRest, result2);
        assertEquals(expectedPriceWithNullRest, result3);
        assertEquals(expectedPriceWithNullRest, result4);
        assertEquals(expectedPriceWithNullRest, result5);
        assertEquals(expectedPriceWithNullRest, result6);
    }

    @Test
    public void testPricesWithNullRest() throws ParseException {
        String currency = " PLN";
        String expectedPriceWithNullRest = "12345.00" + currency;

        PriceMapper.setCurrency(currency);

        String result1 = PriceMapper.priceFactory(prices.get(13));
        String result2 = PriceMapper.priceFactory(prices.get(14));
        String result3 = PriceMapper.priceFactory(prices.get(15));
        String result4 = PriceMapper.priceFactory(prices.get(16));

        assertEquals(expectedPriceWithNullRest, result1);
        assertEquals(expectedPriceWithNullRest, result2);
        assertEquals(expectedPriceWithNullRest, result3);
        assertEquals(expectedPriceWithNullRest, result4);
    }
}
