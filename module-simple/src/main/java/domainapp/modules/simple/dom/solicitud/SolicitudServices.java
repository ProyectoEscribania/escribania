package domainapp.modules.simple.dom.solicitud;


import domainapp.modules.simple.SimpleModule;

import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;

import lombok.RequiredArgsConstructor;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.NotPersistent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Named(SimpleModule.NAMESPACE + ".SolicituService")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "Solicitud", menuBar = DomainServiceLayout.MenuBar.PRIMARY)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class SolicitudServices {

    @Inject JugadorServices jugadorServices;
    @Inject PartidoServices partidoServices;
    @Inject @NotPersistent RepositoryService repositoryService;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Solicitud crearSolicitud(final LocalDate dia, final Horarios horario, final String telefono) {

        NumeroCancha numeroCancha = partidoServices.definirCancha(dia, horario);

        //Precio de la canchaaaa
        Double precio = 20000.0;

        Jugador jugador = jugadorServices.buscarJugador(telefono);

        Solicitud solicitud = haySolicitud(horario, dia, numeroCancha,precio,jugador);
        solicitud.getJugadores().add(0, jugador);

        if (solicitud.getJugadores().size() > 7) {partidoServices.crearPartido(horario, dia, solicitud.getRepresentante().getTelefono(), precio);}

        return repositoryService.persist(solicitud);
    }


    public Solicitud haySolicitud(final Horarios horario, final LocalDate dia, final NumeroCancha numeroCancha,final Double precio, final Jugador jugador) {
        List<Jugador> jugadores = new ArrayList<>();
        return repositoryService.uniqueMatch(
                Query.named(Solicitud.class, Solicitud.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", numeroCancha)
        ).orElse(
                Solicitud.crearSolicitud(dia, horario, numeroCancha, precio, Estados.MATCHMAKING, jugador, jugadores)
        );
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public  List<Solicitud> verSolicitudes(){return repositoryService.allInstances(Solicitud.class);}






}
