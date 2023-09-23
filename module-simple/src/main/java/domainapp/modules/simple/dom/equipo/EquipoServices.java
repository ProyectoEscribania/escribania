package domainapp.modules.simple.dom.equipo;


import domainapp.modules.simple.SimpleModule;

import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;

import lombok.RequiredArgsConstructor;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.TableDecorator;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;

import org.yaml.snakeyaml.representer.Represent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.NotPersistent;

import java.util.ArrayList;
import java.util.List;

@Named(SimpleModule.NAMESPACE + ".EquipoServices")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "Equipo")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class EquipoServices {

    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent JugadorServices jugadorServices;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Equipo crearEquipo(final String nombre,final String telefono){

        Jugador jugador = jugadorServices.buscarJugador(telefono);
        Double edad = (double) jugadorServices.getEdad(jugador.getTelefono());
        List<Jugador> jugadores = new ArrayList<>();

        return repositoryService.persist(Equipo.crearEquipo(nombre,jugador,edad,jugadores));

    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Equipo buscarEquipo(final String telefono){
        Jugador representante = jugadorServices.buscarJugador(telefono);
        return repositoryService.uniqueMatch(
                Query.named(Equipo.class,Equipo.NAMED_QUERY__FIND_BY_REPRESENTANTE)
                .withParameter("representante", representante)
        ).orElse(null);
    }




}
