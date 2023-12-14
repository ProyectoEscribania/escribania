package domainapp.modules.simple.dom.equipo;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.layout.LayoutConstants;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT;
import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType = IdentityType.DATASTORE)
@Unique(
        name = "Equipo__representante__UNQ", members = {"representante"}
)
@Queries({

        @Query(
                name = Equipo.NAMED_QUERY__FIND_BY_REPRESENTANTE,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.equipo.Equipo " +
                        "WHERE representante == :representante"

        ),

})
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Named(SimpleModule.NAMESPACE + ".Equipo")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
public class Equipo {


    static final String NAMED_QUERY__FIND_BY_REPRESENTANTE = "Equipo.findByRepresentante";
    @Inject @NotPersistent TitleService titleService;
    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent JugadorServices jugadorServices;

    public static Equipo crearEquipo(final String name, final Jugador representante, final Double edadPromedio,final List<Jugador> jugadores) {
        val equipo = new Equipo();
        equipo.setName(name);
        equipo.setRepresentante(representante);
        equipo.setEdadPromedio(edadPromedio);
        equipo.setJugadoresEquipo(jugadores);
        return equipo;
    }

    @Title
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "1")
    @Getter@Setter
    private String name;

    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "2")
    @Getter@Setter
    private Jugador representante;

    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "3")
    @Getter @Setter
    private Double edadPromedio;

    @Property(optionality = Optionality.OPTIONAL, editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "4")
    @Getter @Setter
    private List<Jugador> jugadoresEquipo;

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL
    )
    public List<Equipo> eliminarEquipo() {
        final String title = titleService.titleOf(this);
        repositoryService.remove(this);
        return repositoryService.allInstances(Equipo.class);
    }

    @Action(semantics = NON_IDEMPOTENT)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Equipo agregarJugadorAlEquipo(String telefono){

        Jugador jugador = jugadorServices.buscarJugador(telefono);

        if (!this.jugadoresEquipo.contains(jugador) && jugador != null) {
            this.jugadoresEquipo.add(jugador);
            this.setEdadPromedio((this.getEdadPromedio() + jugador.getEdad())/this.jugadoresEquipo.size());
        }

        return this;
    }


    @Action(semantics = NON_IDEMPOTENT)
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Equipo eliminarJugadorDeEquipo(String telefono) {

        Jugador jugador = this.jugadoresEquipo.stream()
                .filter(j -> j.getTelefono().equals(telefono) && !telefono.equals(this.representante.getTelefono()))
                .findFirst()
                .orElse(null);

        if (jugador != null) {
            this.jugadoresEquipo.remove(jugador);
        }

        return this;
    }




}
