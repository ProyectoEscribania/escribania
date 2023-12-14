package domainapp.modules.simple.dom.solicitud.simple;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.Column;
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
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType = IdentityType.DATASTORE)
@Unique(
        name = "Solicitud__horario__UNQ", members = {"horario", "dia", "numeroCancha"}
)
@Queries({

        @Query(
                name = Solicitud.NAMED_QUERY__FIND_BY_NAME_EXACT,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.solicitud.simple.Solicitud " +
                        "WHERE horario == :horario && dia == :dia && numeroCancha == :numeroCancha"
        ),

}
)


@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@Named(SimpleModule.NAMESPACE + ".Solicitud")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
public class Solicitud {

    static final String NAMED_QUERY__FIND_BY_NAME_EXACT = "Solicitud.findByNameExact";
    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent TitleService titleService;
    @Inject @NotPersistent MessageService messageService;

    public static Solicitud crearSolicitud(final LocalDate dia,final  Horarios horario,final  NumeroCancha numeroCancha,final  Double precio,final  Estados estado,final  List<Jugador> jugadores) {
        val solicitud = new Solicitud();
        solicitud.setDia(dia);
        solicitud.setHorario(horario);
        solicitud.setNumeroCancha(numeroCancha);
        solicitud.setPrecio(precio);
        solicitud.setEstados(estado);
        solicitud.setJugadores(jugadores);
        return solicitud;
    }




    @Title
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.IDENTITY, sequence = "1")
    private LocalDate dia;

    @Title
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "2")
    private Horarios horario;

    @Title
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "3")
    private NumeroCancha numeroCancha;

    @Property(editing = Editing.ENABLED)
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "4")
    private Double precio;

    @Title
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "5")
    private Estados estados;

    private Double habilidadPromedio;
    private Double edadPromedio;


    @Property(optionality = Optionality.OPTIONAL, editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "6")
    @Column(allowsNull = "true")
    @Getter @Setter
    private List<Jugador> jugadores;



    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public List<Solicitud> cancelarSolicitud() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
        return repositoryService.allInstances(Solicitud.class);
    }


}























