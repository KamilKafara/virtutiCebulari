package pl.promotion.finder.feature.shop.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Shop {
    ALTO,
    AMSO,
    CARINET,
    COMBAT,
    MORELE,
    XKOM,
    ZADOWOLENIE,
    APOLLO,
    VOBIS,
    NEO24,
    KOMPUTRONIK,
    SONY_CENTRE,
    ALL_WELD;

    public static final List<Shop> SHOPS = new ArrayList<>();

    static {
        SHOPS.addAll(Arrays.asList(values()));
    }
}
