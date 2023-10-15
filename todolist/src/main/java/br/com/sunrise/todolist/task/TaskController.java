package br.com.sunrise.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sunrise.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
   private ITaskRepository repository;

    @PostMapping("/")
   public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
    var idUser =request.getAttribute("idUser");
    taskModel.setIdUser((UUID)idUser);

    var currentDate = LocalDateTime.now();
    //10/11/2023 - Curret
    //10/10/2023 - StartAt
    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início /data de término deve ser maior que do a data atual");
    }if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A data de início deve ser menor que do a data atual");
    }
    //se a data está ok v desse.
    var task =  this.repository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);

   }

   @GetMapping("/")
   public List<TaskModel> list(HttpServletRequest request){
    var idUser =request.getAttribute("idUser");
        var tasks = this.repository.findByIdUser((UUID) idUser);
            return tasks ;
   }

   @PutMapping("/{id}")
   public ResponseEntity uptade(@RequestBody TaskModel taskModel,
    HttpServletRequest request, @PathVariable UUID id){

    var task = this.repository.findById(id).orElse(null);

    if(task ==null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Tarefa não encontrada");
    }

    var idUser =request.getAttribute("idUser");

    if(task.getIdUser().equals(idUser)){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
         .body("Usuário não tem permissão para alterar essa tarefa");
    }

    Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.repository.save(task);
            return ResponseEntity.ok().body(taskUpdated);
   }


}
