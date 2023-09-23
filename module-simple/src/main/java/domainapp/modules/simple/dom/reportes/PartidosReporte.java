package domainapp.modules.simple.dom.reportes;

import lombok.Getter;
import lombok.Setter;

public class PartidosReporte {

    @Getter@Setter
    private String horario;

    @Getter@Setter
    private String dia;

    @Getter@Setter
    private String numeroCancha;

    @Getter@Setter
    private Double precio;

    @Getter@Setter
    private String telefono;

    @Getter@Setter
    private String estados;

    public PartidosReporte(String horario, String dia, String numeroCancha, Double precio, String telefono, String estados) {
        this.horario = horario;
        this.dia = dia;
        this.numeroCancha = numeroCancha;
        this.precio = precio;
        this.telefono = telefono;
        this.estados = estados;
    }

    public PartidosReporte() {
    }
}
