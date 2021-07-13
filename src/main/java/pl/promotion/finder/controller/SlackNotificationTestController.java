package pl.promotion.finder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.promotion.finder.feature.slackbot.SlackMessageSender;
import pl.promotion.finder.feature.slackbot.SupportChannelService;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.io.IOException;

@Controller
@RequestMapping("/slack")
public class SlackNotificationTestController {
    private final SlackMessageSender slackMessageSender;
    private final SupportChannelService supportChannelService;

    public SlackNotificationTestController(SlackMessageSender slackMessageSender, SupportChannelService supportChannelService) {
        this.slackMessageSender = slackMessageSender;
        this.supportChannelService = supportChannelService;
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
        supportChannelService.registerAnnouncement(supportMessageDTO);
        return slackMessageSender.sendPromotionMessage(message);
    }
}
