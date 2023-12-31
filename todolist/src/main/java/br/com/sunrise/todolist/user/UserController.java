package br.com.sunrise.todolist.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
@RestController
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private IUserRepository iUserRepository;

    @PostMapping("/")
    public ResponseEntity acessoCadastro(@RequestBody UserModel userModel){

        var badRequest = this.iUserRepository.findByUsername(userModel.getUsername());

        if(badRequest != null){
        return ResponseEntity.status(400).body("Usuário Já Existe!");
        }


        var passwordHashCode = BCrypt.withDefaults().
        hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashCode);

        var createRepository = iUserRepository.save(userModel);
        return ResponseEntity.status(200).body(createRepository);
        




    }
}
