package pl.promotion.finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;

import java.io.IOException;

@Controller
@RequestMapping("/slack")
public class SlackNotificationTestController {
    private final SlackMessageSender slackMessageSender;

    public SlackNotificationTestController(SlackMessageSender slackMessageSender) {
        this.slackMessageSender = slackMessageSender;
    }

    @GetMapping("/{message}")
    public String sendMessage(@PathVariable("message") String message) throws IOException {
        return slackMessageSender.sendPromotionMessage(message);
    }
}
