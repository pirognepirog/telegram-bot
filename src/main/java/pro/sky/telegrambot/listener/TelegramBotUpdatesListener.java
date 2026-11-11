package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final NotificationTaskService taskService; // <- Добавили сервис

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService taskService) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);

                if (update.message() != null && update.message().text() != null) {
                    String text = update.message().text();
                    Long chatId = update.message().chat().id();

                    if ("/start".equals(text)) {
                        telegramBot.execute(new SendMessage(chatId,
                                "Привет! Отправь мне задачу в формате:\n01.01.2022 20:00 Сделать домашнюю работу"));
                    } else {
                        // Пытаемся создать задачу
                        boolean isCreated = taskService.createTaskFromMessage(chatId, text);

                        if (isCreated) {
                            telegramBot.execute(new SendMessage(chatId, "Задача успешно сохранена! Я вам напомню об этом"));
                        } else {
                            telegramBot.execute(new SendMessage(chatId,
                                    "Неверный формат. Попробуй так:\n01.01.2022 20:00 Сделать домашнюю работу!"));
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}