package domainapp.modules.simple.dom.encargado;


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


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType=IdentityType.DATASTORE)
@Unique(
        name = "Encargado__dni__UNQ", members = { "dni" }
)
@Queries({

        @Query(
                name = Encargado.NAMED_QUERY__FIND_BY_NAME_EXACT,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.encargado.Encargado " +
                        "WHERE telefono == :telefono"
        )
})
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@Named(SimpleModule.NAMESPACE + ".Encargado")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
public class Encargado{


    static final String NAMED_QUERY__FIND_BY_NAME_EXACT = "Encargado.findByNameExact";

    public static Encargado withName(final String nombre,final String apellido, final String dni,final String telefono,final String password) {
        val encargado = new Encargado();
        encargado.setNombre(nombre);
        encargado.setApellido(apellido);
        encargado.setDni(dni);
        encargado.setTelefono(telefono);
        encargado.setPassword(password);

        return encargado;
    }

    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent TitleService titleService;

    @Title
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.IDENTITY, sequence = "1")
    private String nombre;

    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "2")
    private String apellido;

    @Getter@Setter
    private String password;

    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "3")
    private String dni;

    @Property
    @Getter@Setter
    private String telefono;

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public List<Encargado> eliminarEncargado() {
        final String title = titleService.titleOf(this);
        repositoryService.removeAndFlush(this);
        return repositoryService.allInstances(Encargado.class);
    }


}


