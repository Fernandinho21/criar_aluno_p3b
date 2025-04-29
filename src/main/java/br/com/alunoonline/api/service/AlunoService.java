package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    AlunoRepository alunoRepository;

    public void criarAluno(Aluno aluno) {
        alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodosAlunos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> BuscarAlunoPorId(long id) {
        return alunoRepository.findById(id);
    }

    public void deletarAluno(Long id) {
        alunoRepository.deleteById(id);
    }

    public void atualizarAlunoPorId(Long id, Aluno aluno) {

        Optional<Aluno> alunoDoBancoDeDados = BuscarAlunoPorId(id);

        if (alunoDoBancoDeDados.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados.");
        }

        Aluno alunoParaEditar = alunoDoBancoDeDados.get();

        alunoParaEditar.setNome(aluno.getNome());
        alunoParaEditar.setEmail(aluno.getEmail());
        alunoParaEditar.setCpf(aluno.getCpf());

        alunoRepository.save(alunoParaEditar);
    }
}