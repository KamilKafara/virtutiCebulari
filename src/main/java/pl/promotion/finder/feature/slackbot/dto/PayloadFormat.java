package pl.promotion.finder.feature.slackbot.dto;

public class PayloadFormat {
    private PayloadFormat() {
    }

    public static String buildPayload(String message) {
        return "{\"text\":\"" + message + "!\"}";
    }
}
