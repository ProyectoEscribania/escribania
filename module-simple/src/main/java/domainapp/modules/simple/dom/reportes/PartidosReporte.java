package domainapp.modules.simple.dom.reportes;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.layout.LayoutConstants;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.Column;

import java.time.LocalDate;

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
