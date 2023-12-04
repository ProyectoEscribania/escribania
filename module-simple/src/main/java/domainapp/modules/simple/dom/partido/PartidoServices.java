package domainapp.modules.simple.dom.partido;

import java.time.LocalDate;
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
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;


@Named(SimpleModule.NAMESPACE + ".PartidoServices")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "Partido")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PartidoServices {

    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent JugadorServices jugadorServices;
    @Inject @NotPersistent MessageService messageService;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-plus")
    public Partido crearPartido(final String horarioSting, final String diaString, final String telefono, final Double precio) {
        Horarios horario = Horarios.valueOf(horarioSting);
        LocalDate dia = LocalDate.parse(diaString);
        NumeroCancha numeroCancha = definirCancha(diaString, horarioSting);
        Jugador representante = jugadorServices.buscarJugador(telefono);
        return repositoryService.persist(Partido.crearTurno(horario, dia, numeroCancha, representante, precio));
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-plus")
    public Partido sacarTurno(final String horarioSting, final String diaString, final String telefono) {

        Horarios horario = Horarios.valueOf(horarioSting);
        LocalDate dia = LocalDate.parse(diaString);

        Jugador representante = jugadorServices.buscarJugador(telefono);


        if (!hayPartido(telefono).isEmpty()) {
            messageService.warnUser("YA EXISTE UN PARTIDO RESERVADO A TU NOMBRE");
            return hayPartido(telefono).get(0);
        }

        NumeroCancha numeroCancha = definirCancha(diaString, horarioSting);
        return repositoryService.persist(Partido.pedirTurno(horario, dia, numeroCancha, representante));
    }


    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public Partido buscarPartido(final String horarioSting, final String diaString, final String numeroCanchaSting) {
        Horarios horario = Horarios.valueOf(horarioSting);
        LocalDate dia = LocalDate.parse(diaString);
        NumeroCancha numeroCancha = NumeroCancha.valueOf(numeroCanchaSting);
        return repositoryService.uniqueMatch(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", numeroCancha)
        ).orElse(null);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public List<Partido> buscarPartidoPorRepresentante(final String telefono) {
        Jugador representante = jugadorServices.buscarJugador(telefono);
        return repositoryService.allMatches(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_REPRESENTANTE)
                        .withParameter("representante", representante));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-list")
    public List<Partido> verPartidos() {
        return repositoryService.allInstances(Partido.class);
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public List<Partido> buscarPartidosPorEstados(final String estadosString) {
        Estados estados = Estados.valueOf(estadosString);
        return repositoryService.allMatches(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_ESTADO)
                        .withParameter("estados", estados));
    }


    public List<Partido> hayPartido(final String telefono) {
        Jugador jugador = jugadorServices.buscarJugador(telefono);
        return (repositoryService.allMatches(Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_ESTADO_AND_REPRESENTANTE)
                .withParameter("representante", jugador)
                .withParameter("estados", Estados.CONFIRMADO)
                .withParameter("estados2", Estados.ESPERA)));
    }

    public NumeroCancha definirCancha(final String dia, final String horario) {
        NumeroCancha numeroCancha = NumeroCancha.TRES;
        if (buscarPartido(horario, dia, "UNO") == null) numeroCancha = NumeroCancha.UNO;
        else if (buscarPartido(horario, dia, "DOS") == null) numeroCancha = NumeroCancha.DOS;
        return numeroCancha;
    }


    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, cssClassFa = "fa-search")
    public boolean existePartido3(final String horarioS, final String diaS) {

        Horarios horario = Horarios.valueOf(horarioS);
        LocalDate dia = LocalDate.parse(diaS);

        return repositoryService.uniqueMatch(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", NumeroCancha.TRES)
        ).orElse(null) == null;
    }

}