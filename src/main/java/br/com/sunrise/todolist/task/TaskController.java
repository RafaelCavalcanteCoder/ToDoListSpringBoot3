package br.com.sunrise.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taks")
public class TaskController {

    @Autowired
   private ITaskRepository repository;


   public TaskModel create(@RequestBody TaskModel taskModel){
   var teste =  this.repository.save(taskModel);
    return teste;

   }



}
