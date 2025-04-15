package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    AlunoService alunoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void CriarAluno(@RequestBody Aluno aluno) {
        alunoService.criarAluno(aluno);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Aluno> ListarTodosAlunos(){
        return alunoService.listarTodosAlunos();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Aluno> buscarAlunoPorId(@PathVariable long id){
        return alunoService.BuscarAlunoPorId(id);
    }
}

