package br.com.sunrise.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.sunrise.todolist.task.ITaskRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private ITaskRepository repository;
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
         //Pegaa a  Autenticação do usúario e senha.

         //pegando o numero da Autorização depois de mandar um request via postman com usuario e senha digitados.
        var authorization = request.getHeader("Authorization");
        //contando o numero da Autorização a partir do Basic pra frente.
        var authEnconded = authorization.substring("Basic".length()).trim();
        //Pegando esse código para ser codificado em byts
        byte[] authDecode  = Base64.getDecoder().decode(authEnconded);
        //guardando dentro de uma viariavel String e passando como parametro Autentação Codificada.
        var authString = new String(authDecode);
        //Tirando o : para melhor visualização.
        String[] credentials = authString.split(":");
        String username = credentials[0];// pega apartir da posicação zero
        String password = credentials[1]; // posição 1
    

                //Validação de Usuário

                var user = repository.findByUsername(username);
                if (user == null){
                    response.sendError(401, "Usuário Sem Autorização");
                }else{

                   var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                }
                //Validação de Senha

                //Tudo OK ?> Segue Viagem

                //Erro na Validação ? Mensagem de Erro.


    

        filterChain.doFilter(request, response);


    }
    }

   
    


