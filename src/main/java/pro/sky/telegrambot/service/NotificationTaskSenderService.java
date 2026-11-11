package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationTaskSenderService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTaskSenderService.class);

    private final NotificationTaskRepository repository;
    private final TelegramBot telegramBot;

    public NotificationTaskSenderService(NotificationTaskRepository repository, TelegramBot telegramBot) {
        this.repository = repository;
        this.telegramBot = telegramBot;
    }

    // @Scheduled(cron = "0 * * * * *") означает: "выполнять в 0 секунд каждой минуты"
    @Scheduled(cron = "0 * * * * *")
    public void sendNotifications() {
        logger.debug("Checking for tasks to send...");

        // Округляем текущее время до минут
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        logger.info("Looking for tasks at time: {}", now);

        // Ищем задачи в БД, время которых совпадает с текущей минутой
        List<NotificationTask> tasks = repository.findAllByDateTime(now);

        if (tasks.isEmpty()) {
            return; // Если задач нет, просто выходим
        }

        for (NotificationTask task : tasks) {
            try {
                // Отправляем сообщение в Telegram
                SendMessage request = new SendMessage(task.getChatId(), task.getMessage());
                telegramBot.execute(request);
                logger.info("Notification sent to chatId {}: {}", task.getChatId(), task.getMessage());

                // Опционально: можно удалить задачу после отправки, чтобы не спамить
                // repository.delete(task);

            } catch (Exception e) {
                logger.error("Failed to send notification for task id {}: {}", task.getId(), e.getMessage());
            }
        }
    }
}