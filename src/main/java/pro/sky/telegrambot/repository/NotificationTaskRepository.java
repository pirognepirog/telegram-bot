package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    // Найти все задачи, у которых время напоминания совпадает с переданным
    List<NotificationTask> findAllByDateTime(LocalDateTime dateTime);


}