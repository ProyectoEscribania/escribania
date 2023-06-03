package domainapp.modules.simple.dom.jugador;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import domainapp.modules.simple.dom.partido.Partido;

import org.apache.causeway.applib.annotation.Navigable;
import org.apache.causeway.applib.annotation.Title;

import org.springframework.lang.Nullable;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.TableDecorator;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.layout.LayoutConstants;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.applib.value.Blob;
import org.apache.causeway.extensions.pdfjs.applib.annotations.PdfJsViewer;

import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import domainapp.modules.simple.SimpleModule;


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType=IdentityType.DATASTORE)
@Unique(
        name = "Jugador__telefono__UNQ", members = { "telefono" }
)
@Queries({

        @Query(
                name = Jugador.NAMED_QUERY__FIND_BY_TEL,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.jugador.Jugador " +
                        "WHERE telefono == :telefono"
        )
})
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@Version(strategy= VersionStrategy.DATE_TIME, column="version")
@Named(SimpleModule.NAMESPACE + ".Jugador")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout(tableDecorator = TableDecorator.DatatablesNet.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Jugador implements Comparable<Jugador> {


    static final String NAMED_QUERY__FIND_BY_TEL = "Jugador.findByTel";


    public static Jugador withName(final String nombre,final String apellido,final String telefono,final String mail,final String password) {
        val jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setApellido(apellido);
        jugador.setTelefono(telefono);
        jugador.setMail(mail);
        jugador.setPassword(password);
        return jugador;
    }

    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent TitleService titleService;
    @Inject @NotPersistent MessageService messageService;



    @Title
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.IDENTITY, sequence = "1")
    private String nombre;

    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "2")
    private String apellido;


    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "3")
    private String telefono;


    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "4")
    private String mail;

    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "5")
    @Getter @Setter
    @Persistent(mappedBy = "representante")
    private List<Partido> partidos;

    @Property
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "6")
    @Getter@Setter
    private String password;




    @PdfJsViewer
    @Getter @Setter
    @Persistent(defaultFetchGroup="false", columns = {
            @Column(name = "attachment_name"),
            @Column(name = "attachment_mimetype"),
            @Column(name = "attachment_bytes")
    })
    @Property()
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    private Blob attachment;



    static final String PROHIBITED_CHARACTERS = "&%$!";



    @Action(semantics = IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "attachment", position = ActionLayout.Position.PANEL)
    public Jugador updateAttachment(
            @Nullable final Blob attachment) {
        setAttachment(attachment);
        return this;
    }
    @MemberSupport public Blob default0UpdateAttachment() {
        return getAttachment();
    }

//    @Action
//    @ActionLayout(associateWith = "partido", position = ActionLayout.Position.PANEL)
//    public Partido verPartido(){
//        return partido;
//    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }



    private final static Comparator<Jugador> comparator =
            Comparator.comparing(Jugador::getTelefono);

    @Override
    public int compareTo(final Jugador other) {
        return comparator.compare(this, other);
    }

}
