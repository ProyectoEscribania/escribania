package domainapp.modules.simple.dom.jugador;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.JDOQLTypedQuery;

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
import domainapp.modules.simple.dom.jugador.types.Name;

@Named(SimpleModule.NAMESPACE + ".JugadorService")
@DomainService(nature = NatureOfService.VIEW)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class JugadorServices {

    final RepositoryService repositoryService;
    final JdoSupportService jdoSupportService;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Jugador crearJugador(
            final String nombre,final String apellido,final String telefono,final String mail) {
        return repositoryService.persist(Jugador.withName(nombre,apellido,telefono,mail));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)

    public Jugador buscarJugador(final String telefono) {
        return repositoryService.firstMatch(
                    Query.named(Jugador.class, Jugador.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("telefono", telefono))
                .orElse(null);
    }



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Jugador> mostrarTodos() {
        return repositoryService.allInstances(Jugador.class);
    }

    public void ping() {
        JDOQLTypedQuery<Jugador> q = jdoSupportService.newTypesafeQuery(Jugador.class);
        final QJugador candidate = QJugador.candidate();
        q.range(0,2);
        q.orderBy(candidate.telefono.asc());
        q.executeList();
    }



}
