package domainapp.modules.simple.dom.partido;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.JDOQLTypedQuery;

import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;

import domainapp.modules.simple.dom.so.SimpleObject;

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
import org.apache.causeway.persistence.jdo.applib.services.JdoSupportService;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;

@Named(SimpleModule.NAMESPACE + ".PartidoServices")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "Partido", menuBar = DomainServiceLayout.MenuBar.PRIMARY)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PartidoServices {

    final RepositoryService repositoryService;
    final JdoSupportService jdoSupportService;
    final JugadorServices jugadorServices;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-plus")
    public Partido crearPartido(
            final Horarios horario, final LocalDate dia, final String telefono, final double precio) {

        NumeroCancha numeroCancha = NumeroCancha.Tres;
        if (buscarPartido(horario, dia, NumeroCancha.Uno) == null) {
            numeroCancha = NumeroCancha.Uno;
        } else if (buscarPartido(horario, dia, NumeroCancha.Dos) == null) {
            numeroCancha = NumeroCancha.Dos;
        }

        Jugador representante = jugadorServices.buscarJugador(telefono);


        return repositoryService.persist(Partido.crearTurno(horario, dia, numeroCancha, representante, precio));
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-plus")
    public Partido sacarTurno(
            final Horarios horario, final LocalDate dia, final String telefono) {


        NumeroCancha numeroCancha = NumeroCancha.Tres;
        if (buscarPartido(horario, dia, NumeroCancha.Uno) == null) {
            numeroCancha = NumeroCancha.Uno;
        } else if (buscarPartido(horario, dia, NumeroCancha.Dos) == null) {
            numeroCancha = NumeroCancha.Dos;
        }

        Jugador representante = jugadorServices.buscarJugador(telefono);

        return repositoryService.persist(Partido.pedirTurno(horario, dia, numeroCancha, representante));
    }


    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public Partido buscarPartido(final Horarios horario, final LocalDate dia, final NumeroCancha numeroCancha) {
        return repositoryService.uniqueMatch(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", numeroCancha)
        ).orElse(null);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public Partido buscarPartidoPorRepresentante(final String telefono) {

        Jugador representante = jugadorServices.buscarJugador(telefono);

        return repositoryService.uniqueMatch(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_REPRESENTANTE)
                        .withParameter("representante", representante)
        ).orElse(null);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-list")
    public List<Partido> verPartidos() {
        return repositoryService.allInstances(Partido.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public List<Partido> buscarPartidosPorEstados(final Estados estados) {
        return repositoryService.allMatches(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_ESTADO)
                        .withParameter("estados", estados));
    }


    //        @Action
//    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
//    public void añadirJugador(Jugador jugador){
//            ;
//            jugadores.add(jugador);
//        return  jugadores;
//      }
    public void ping() {
        JDOQLTypedQuery<SimpleObject> q = jdoSupportService.newTypesafeQuery(SimpleObject.class);
        final QPartido candidate = QPartido.candidate();
        q.range(0, 2);
        q.orderBy(candidate.horario.asc());
        q.executeList();

    }

}
