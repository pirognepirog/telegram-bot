package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity // данный класс является таблицей к БД
@Table(name = "notification_task") // явно задаю название таблицы
public class NotificationTask {

    // разворачиваем таблицу в БД
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID чата в Telegram, чтобы знать, куда отправлять напоминание
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    // Текст напоминания
    @Column(name = "message", nullable = false)
    private String message;

    // Дата и время, когда нужно отправить напоминание
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    // Конструктор без аргументов (обязателен для JPA)
    public NotificationTask() {
    }

    // Конструктор для создания задачи
    public NotificationTask(Long chatId, String message, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.message = message;
        this.dateTime = dateTime;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, dateTime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}