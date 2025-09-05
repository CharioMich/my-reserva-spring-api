package gr.aueb.cf.myreserva.dto;

public record ResponseMessageDTO (
        String code,
        String description
){
    public ResponseMessageDTO(String code) {
        this(code, ""); // delegates to canonical constructor provided by the java record
    }
}
