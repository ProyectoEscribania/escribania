package domainapp.modules.simple.dom.jugador;

import java.time.LocalDate;
import java.time.Period;

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
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.layout.LayoutConstants;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.equipo.Equipo;
import domainapp.modules.simple.dom.equipo.EquipoServices;


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType = IdentityType.DATASTORE)
@Unique(
        name = "Jugador__telefono__UNQ", members = {"telefono"}
)
@Queries({

        @Query(
                name = Jugador.NAMED_QUERY__FIND_BY_TEL,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.jugador.Jugador " +
                        "WHERE telefono == :telefono"
        )
})
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Named(SimpleModule.NAMESPACE + ".Jugador")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
public class Jugador{

    static final String NAMED_QUERY__FIND_BY_TEL = "Jugador.findByTel";
    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent TitleService titleService;
    @Inject @NotPersistent EquipoServices equipoServices;



    public static Jugador crearJugador(final String nombre, final String apellido, final String telefono, final String mail, final String password, final LocalDate fechaDeNacimiento) {
        val jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setApellido(apellido);
        jugador.setTelefono(telefono);
        jugador.setMail(mail);
        jugador.setPassword(password);
        jugador.setFechaNacimiento(fechaDeNacimiento);
        jugador.setEdad((double) Period.between(fechaDeNacimiento, LocalDate.now()).getYears());
        return jugador;
    }




    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.IDENTITY, sequence = "1")
    private String nombre;

    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "2")
    private String apellido;

    @Title
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "3")
    private String telefono;

    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "4")
    private String mail;

    @Getter @Setter
    private String password;

    @Property(optionality = Optionality.OPTIONAL)
    @Getter@Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "7")
    private LocalDate fechaNacimiento;

    @Getter@Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "8")
    private Double edad;


    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL)
    public void eliminarJugador() {
        final String title = titleService.titleOf(this);
        Equipo equipo = equipoServices.buscarEquipo(this.getTelefono());
        repositoryService.removeAndFlush(equipo);
        repositoryService.removeAndFlush(this);


    }


}
