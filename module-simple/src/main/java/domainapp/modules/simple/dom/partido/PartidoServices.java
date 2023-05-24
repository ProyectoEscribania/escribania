package domainapp.modules.simple.dom.partido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.JDOQLTypedQuery;

import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;
import domainapp.modules.simple.dom.so.SimpleObject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
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
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PartidoServices {

    final RepositoryService repositoryService;
    final JdoSupportService jdoSupportService;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public void crearPartido(
            final Horarios horario, final BigDecimal precio, final NumeroCancha numeroCancha, final LocalDate dia) {

        try {
             repositoryService.persist(Partido.withName(horario, dia, numeroCancha, precio));
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }




    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Partido buscarPartido(final Horarios horario, final LocalDate dia, final NumeroCancha numeroCancha ) {
        return repositoryService.uniqueMatch(
                    Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                            .withParameter("dia", dia)
                            .withParameter("numeroCancha", numeroCancha)
                )
                .orElse(null);
    }



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Partido> verPartidos() {
        return repositoryService.allInstances(Partido.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Partido> verPartidosEnEspera() {
        return repositoryService.allMatches(
                Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_ESTADO_ESPERA));

    }


//    @Action
//    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
//    public List<Jugador> añadirJugador(Jugador jugador){
//            List<Jugador> jugadores = new ArrayList<Jugador>();
//            jugadores.add(jugador);
//        return  jugadores;
  //  }
    public void ping() {
        JDOQLTypedQuery<SimpleObject> q = jdoSupportService.newTypesafeQuery(SimpleObject.class);
        final QPartido candidate = QPartido.candidate();
        q.range(0, 2);
        q.orderBy(candidate.horario.asc());
        q.executeList();

    }

}
