package br.com.alunoonline.api.service;

import dtos.AtualizarNotasRequestDTO;
import dtos.DisciplinasAlunoResponseDTO;
import dtos.HistoricoAlunoResponseDTO;

import br.com.alunoonline.api.enums.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class MatriculaAlunoService {

    public static final double MEDIA_PARA_APROVACAO = 7.0;
    public static final int QNT_NOTAS = 2;

    @Autowired
    MatriculaAlunoRepository matriculaAlunoRepository;

    public void criarMatricula(MatriculaAluno matriculaAluno) {
        matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
        matriculaAlunoRepository.save(matriculaAluno);
    }

    public void trancarMatricula(Long matriculaAlunoId) {
        MatriculaAluno matriculaAluno = buscarMatriculaOuLancarExcecao(matriculaAlunoId);

        if (matriculaAluno.getStatus().equals(MatriculaAlunoStatusEnum.MATRICULADO)) {
            matriculaAluno.setStatus(MatriculaAlunoStatusEnum.TRANCADO);
            matriculaAlunoRepository.save(matriculaAluno);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Só é possível trancar o curso com o status MATRICULADO");
        }
    }

    public void atualizarNotas(Long matriculaAlunoId, dtos.AtualizarNotasRequestDTO request) {
        MatriculaAluno matriculaAluno = buscarMatriculaOuLancarExcecao(matriculaAlunoId);

        if (request.getNota1() != null) {
            matriculaAluno.setNota1(request.getNota1());
        }

        if (request.getNota2() != null) {
            matriculaAluno.setNota2(request.getNota2());
        }

        calculaMediaEModificaStatus(matriculaAluno);
        matriculaAlunoRepository.save(matriculaAluno);
    }

    public HistoricoAlunoResponseDTO emitirHistorico(Long alunoId) {
        List<MatriculaAluno> matriculas = matriculaAlunoRepository.findByAlunoId(alunoId);

        if (matriculas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matrículas");
        }

        MatriculaAluno primeiraMatricula = matriculas.get(0);

        HistoricoAlunoResponseDTO historico = new HistoricoAlunoResponseDTO();
        historico.setNomeAluno(primeiraMatricula.getAluno().getNome());
        historico.setEmailAluno(primeiraMatricula.getAluno().getEmail());
        historico.setCpfAluno(primeiraMatricula.getAluno().getCpf());

        List<DisciplinasAlunoResponseDTO> disciplinas = new ArrayList<>();
        for (MatriculaAluno matricula : matriculas) {
            disciplinas.add(mapearParaDisciplinasAlunoResponseDTO(matricula));
        }

        historico.setDisciplinasAlunoResponsesDTO(disciplinas);
        return historico;
    }

    private void calculaMediaEModificaStatus(MatriculaAluno matriculaAluno) {
        Double nota1 = matriculaAluno.getNota1();
        Double nota2 = matriculaAluno.getNota2();

        if (nota1 != null && nota2 != null) {
            double media = (nota1 + nota2) / QNT_NOTAS;

            if (media >= MEDIA_PARA_APROVACAO) {
                matriculaAluno.setStatus(MatriculaAlunoStatusEnum.APROVADO);
            } else {
                matriculaAluno.setStatus(MatriculaAlunoStatusEnum.REPROVADO);
            }
        }
    }

    private MatriculaAluno buscarMatriculaOuLancarExcecao(Long id) {
        return matriculaAlunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada"));
    }

    private DisciplinasAlunoResponseDTO mapearParaDisciplinasAlunoResponseDTO(MatriculaAluno matricula) {
        DisciplinasAlunoResponseDTO dto = new DisciplinasAlunoResponseDTO();
        dto.setNomeDisciplina(matricula.getDisciplina().getNome());
        dto.setNomeProfessor(matricula.getDisciplina().getProfessor().getNome());
        dto.setNota1(matricula.getNota1());
        dto.setNota2(matricula.getNota2());
        dto.setMedia(calcularMedia(matricula.getNota1(), matricula.getNota2()));
        dto.setStatus(matricula.getStatus());
        return dto;
    }

    private Double calcularMedia(Double nota1, Double nota2) {
        return (nota1 != null && nota2 != null) ? (nota1 + nota2) / QNT_NOTAS : null;
    }
}
