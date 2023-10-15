package br.com.sunrise.todolist.task;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sunrise.todolist.user.UserModel;
import java.util.List;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {

    UserModel findByUsername(String taskModel);
    List<TaskModel> findByIdUser(UUID idUser);
    
}
