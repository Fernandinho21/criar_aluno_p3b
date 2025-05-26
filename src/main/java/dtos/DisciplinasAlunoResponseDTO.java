package dtos;

import br.com.alunoonline.api.enums.MatriculaAlunoStatusEnum;
import lombok.Data;

@Data
public class DisciplinasAlunoResponseDTO {
    private String nomeDisciplina;
    private String nomeProfessor;
    private Double nota1;
    private Double nota2;
    private Double media;
    private MatriculaAlunoStatusEnum status;

    // Construtor padrão
    public DisciplinasAlunoResponseDTO() {
    }

    // Construtor com parâmetros (opcional)
    public DisciplinasAlunoResponseDTO(String nomeDisciplina, String nomeProfessor,
                                       Double nota1, Double nota2, Double media,
                                       MatriculaAlunoStatusEnum status) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.media = media;
        this.status = status;
    }
}