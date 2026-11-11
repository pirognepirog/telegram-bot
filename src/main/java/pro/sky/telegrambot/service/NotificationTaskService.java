package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class NotificationTaskService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    // Формат, в котором пользователь присылает дату
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationTaskRepository repository;

    public NotificationTaskService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Парсит сообщение и сохраняет задачу.
     * @return true, если задача успешно создана, false - если формат неверный.
     */
    public boolean createTaskFromMessage(Long chatId, String text) {
        // Проверяем, что сообщение достаточно длинное (минимум 17 символов: "01.01.2022 20:00 " и хоть 1 символ текста)
        if (text == null || text.length() < 17) {
            logger.warn("Message is too short or null: {}", text);
            return false;
        }

        try {
            // 1. Отрезаем первые 16 символов (дата и время)
            String dateString = text.substring(0, 16);
            // 2. Берем всё, что идет после 16-го символа (текст задачи)
            String taskMessage = text.substring(17).trim();

            // 3. Превращаем строку в дату
            LocalDateTime dateTime = LocalDateTime.parse(dateString, FORMATTER);

            // 4. Создаем и сохраняем задачу
            NotificationTask task = new NotificationTask(chatId, taskMessage, dateTime);
            repository.save(task);

            logger.info("Task saved successfully: {}", task);
            return true;

        } catch (DateTimeParseException e) {
            logger.warn("Failed to parse date from message: {}", text);
            return false;
        }
    }
}