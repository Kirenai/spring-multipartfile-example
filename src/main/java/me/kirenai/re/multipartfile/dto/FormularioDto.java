package me.kirenai.re.multipartfile.dto;

public class FormularioDto {

    private Long id;

    public FormularioDto() {
    }

    public FormularioDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FormularioDto{" +
                "id=" + id +
                '}';
    }
}
