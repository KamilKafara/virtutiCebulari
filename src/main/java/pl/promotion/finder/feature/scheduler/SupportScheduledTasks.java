package pl.promotion.finder.feature.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.promotion.finder.feature.shopStatus.dto.ShopStatusDTO;
import pl.promotion.finder.feature.shopStatus.service.ShopStatusService;
import pl.promotion.finder.feature.slackbot.SupportMessageMapper;
import pl.promotion.finder.feature.slackbot.SupportMessageSender;
import pl.promotion.finder.feature.slackbot.dto.SupportMessageDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@Service
@AllArgsConstructor
public class SupportScheduledTasks {
    private static final String CRON_DURATION = "0 17 * * *";
    private static final String CURRENT_ZONE = "Europe/Warsaw";

    private final SupportMessageSender supportMessageSender;
    private final ShopStatusService shopStatusService;

    @Scheduled(cron = CRON_DURATION, zone = CURRENT_ZONE)
    public void reportProblems() {
        sendMessageToSupport();
    }

    private void sendMessageToSupport() {
        List<ShopStatusDTO> lastEnableShopsStatus = shopStatusService.getDisableShops();
        SupportMessageMapper messageMapper = new SupportMessageMapper();
        List<SupportMessageDTO> supportMessageDTOList = lastEnableShopsStatus.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());

        supportMessageDTOList.forEach(supportMessageSender::registerAnnouncement);
    }
}
