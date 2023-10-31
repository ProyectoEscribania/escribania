package domainapp.modules.simple.dom.solicitud.equipos;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.NotPersistent;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.equipo.Equipo;
import domainapp.modules.simple.dom.equipo.EquipoServices;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;


@Named(SimpleModule.NAMESPACE + ".SolicitudEquipoServices")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "SolicituEquipo")
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class SolicitudEquipoServices {

    @Inject @NotPersistent JugadorServices jugadorServices;
    @Inject @NotPersistent PartidoServices partidoServices;
    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject EquipoServices equipoServices;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public SolicitudEquipo crearSolicitudEquipo(final String diaString, final String telefono, final String horarioSting) {



        Horarios horario = Horarios.valueOf(horarioSting);
        LocalDate dia = LocalDate.parse(diaString);

        //Precio de la canchaaaa
        Double precio = 20000.0;

        Jugador jugador = jugadorServices.buscarJugador(telefono);

        NumeroCancha numeroCancha = partidoServices.definirCancha(diaString, horarioSting);

        SolicitudEquipo solicitudEquipo = haySolicitud(horario, dia, numeroCancha, precio);



        if (esEquipo1(solicitudEquipo)) {

            solicitudEquipo.setEquipo1(equipoServices.buscarEquipo(telefono));
            return repositoryService.persist(solicitudEquipo);

        } else {
            solicitudEquipo.setEquipo2(equipoServices.buscarEquipo(telefono));
            partidoServices.crearPartido(horarioSting, diaString, solicitudEquipo.getEquipo1().getRepresentante().getTelefono(), precio);
            repositoryService.removeAndFlush(solicitudEquipo);
            return null;
        }
    }


    public SolicitudEquipo haySolicitud(final Horarios horario, final LocalDate dia, final NumeroCancha numeroCancha, final Double precio) {
        Equipo equipo1 = new Equipo();
        Equipo equipo2 = new Equipo();

        return repositoryService.uniqueMatch(
                Query.named(SolicitudEquipo.class, SolicitudEquipo.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", numeroCancha)
        ).orElse(
                SolicitudEquipo.crearSolicitudEquipo(dia, horario, numeroCancha, precio, Estados.MATCHMAKING, equipo1,equipo2)
        );
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<SolicitudEquipo> verSolicitudes() {
        return repositoryService.allInstances(SolicitudEquipo.class);
    }


    public List<Horarios> horariosRestringidos(String diaString) {


        List<Horarios> horariosRestringidos = new ArrayList<>();

        for (Horarios hora : Horarios.values()) {
            if (partidoServices.buscarPartido(String.valueOf(hora), diaString, "TRES") == null) {
                horariosRestringidos.add(hora);
            }
        }
            return horariosRestringidos;
    }

    public boolean esEquipo1(SolicitudEquipo solicitudEquipo) {
        List<Jugador> jugadoresEquipo = solicitudEquipo.getEquipo1().getJugadoresEquipo();
        if (jugadoresEquipo == null) {
            return true;
        } else {
            return false;
        }
    }

}

