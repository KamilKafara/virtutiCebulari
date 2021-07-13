package pl.promotion.finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;
import pl.promotion.finder.feature.slackbot.SupportMessageSender;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.io.IOException;

@Controller
@RequestMapping("/slack")
public class SlackNotificationTestController {
    private final SlackMessageSender slackMessageSender;
    private final SupportMessageSender supportMessageSender;

    public SlackNotificationTestController(SlackMessageSender slackMessageSender, SupportMessageSender supportMessageSender) {
        this.slackMessageSender = slackMessageSender;
        this.supportMessageSender = supportMessageSender;
    }

    @GetMapping("/{message}")
    private String sendMessage(@PathVariable("message") String message) throws IOException {
        return slackMessageSender.sendPromotionMessage(message);
    }

    @GetMapping("/support/{message}")
    private String sendSupportMessage(@PathVariable("message") String message) throws IOException {
        SupportMessageDTO supportMessageDTO = SupportMessageDTO.builder()
                .message(message)
                .path("http://google.com/")
                .shopName("Google")
                .build();
        supportMessageSender.registerAnnouncement(supportMessageDTO);
        return slackMessageSender.sendPromotionMessage(message);
    }
}
