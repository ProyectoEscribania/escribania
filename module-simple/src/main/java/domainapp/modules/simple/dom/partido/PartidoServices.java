package domainapp.modules.simple.dom.partido;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.JDOQLTypedQuery;

import domainapp.modules.simple.dom.partido.types.Horario;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
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
import domainapp.modules.simple.dom.so.types.Name;

@Named(SimpleModule.NAMESPACE + ".SimpleObjects")
@DomainService(nature = NatureOfService.VIEW)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PartidoServices {

    final RepositoryService repositoryService;
    final JdoSupportService jdoSupportService;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Partido create(
            @Horario final LocalDate horario) {
        return repositoryService.persist(Partido.withName(horario));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Partido> findByHorario(
            @Horario final LocalDate horario
            ) {
        return repositoryService.allMatches(
                    Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_LIKE)
                        .withParameter("horario", horario));
    }


    public Partido findByNameExact(final String name) {
        return repositoryService.firstMatch(
                    Query.named(Partido.class, Partido.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("name", name))
                .orElse(null);
    }



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(associateWith = )
    public List<Partido> listAll() {
        return repositoryService.allInstances(Partido.class);
    }



    public void ping() {
        JDOQLTypedQuery<Partido> q = jdoSupportService.newTypesafeQuery(Partido.class);
        final QPartido candidate = QPartido.candidate();
        q.range(0,2);
        q.orderBy(candidate.horario.asc());
        q.executeList();
    }

}
