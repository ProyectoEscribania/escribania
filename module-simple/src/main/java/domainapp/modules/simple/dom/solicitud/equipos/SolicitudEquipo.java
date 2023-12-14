package domainapp.modules.simple.dom.solicitud.equipos;

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
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.TableDecorator;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.layout.LayoutConstants;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.equipo.Equipo;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;


@PersistenceCapable(
        schema = SimpleModule.SCHEMA,
        identityType = IdentityType.DATASTORE)
@Unique(
        name = "SolicitudEquipo__horario__UNQ", members = {"horario", "dia", "numeroCancha"}
)
@Queries({

        @Query(
                name = SolicitudEquipo.NAMED_QUERY__FIND_BY_NAME_EXACT,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.solicitud.equipos.SolicitudEquipo " +
                        "WHERE horario == :horario && dia == :dia && numeroCancha == :numeroCancha"
        ),
        @Query(
                name = SolicitudEquipo.NAMED_QUERY__FIND_BY_EQUIPO1,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.solicitud.equipos.SolicitudEquipo " +
                        "WHERE equipo1 == :equipo1"
        ),
        @Query(
                name = SolicitudEquipo.NAMED_QUERY__FIND_BY_EQUIPO2,
                value = "SELECT " +
                        "FROM domainapp.modules.simple.dom.solicitud.equipos.SolicitudEquipo " +
                        "WHERE equipo2 == :equipo2"
        )

}
)


@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@Version(strategy = VersionStrategy.DATE_TIME, column = "version")
@Named(SimpleModule.NAMESPACE + ".SolicitudEquipo")
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout(tableDecorator = TableDecorator.DatatablesNet.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class SolicitudEquipo {

    static final String NAMED_QUERY__FIND_BY_NAME_EXACT = "Solicitud.findByNameExact";
    static final String NAMED_QUERY__FIND_BY_EQUIPO1 = "Solicitud.findByEquipo1";
    static final String NAMED_QUERY__FIND_BY_EQUIPO2 = "Solicitud.findByEquipo2";
    public static SolicitudEquipo crearSolicitudEquipo(final LocalDate dia, final  Horarios horario, final  NumeroCancha numeroCancha, final  Double precio, final  Estados estado, final  Equipo equipo1, final Equipo equipo2) {
        val solicitud = new SolicitudEquipo();
        solicitud.setDia(dia);
        solicitud.setHorario(horario);
        solicitud.setNumeroCancha(numeroCancha);
        solicitud.setPrecio(precio);
        solicitud.setEstados(estado);
        solicitud.setEquipo1(equipo1);
        solicitud.setEquipo2(null);
        return solicitud;
    }

    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject @NotPersistent TitleService titleService;
    @Inject @NotPersistent MessageService messageService;


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

    @Title
    @Getter @Setter
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "6")
    private Double edadPromedio;


    @Property(optionality = Optionality.OPTIONAL, editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "7")
    @Column(allowsNull = "true")
    @Getter @Setter
    private Equipo equipo1;

    @Property(optionality = Optionality.OPTIONAL, editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = LayoutConstants.FieldSetId.DETAILS, sequence = "8")
    @Column(allowsNull = "true")
    @Getter @Setter
    private Equipo equipo2;




    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            fieldSetId = LayoutConstants.FieldSetId.IDENTITY,
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public List<SolicitudEquipo> cancelarSolicitud() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
        return repositoryService.allInstances(SolicitudEquipo.class);
    }


}























