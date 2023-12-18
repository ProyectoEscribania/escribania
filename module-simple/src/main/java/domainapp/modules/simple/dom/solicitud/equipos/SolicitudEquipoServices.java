package domainapp.modules.simple.dom.solicitud.equipos;


import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.annotations.NotPersistent;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.equipo.Equipo;
import domainapp.modules.simple.dom.equipo.EquipoServices;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import domainapp.modules.simple.dom.partido.types.Horarios;
import domainapp.modules.simple.dom.partido.types.NumeroCancha;
import domainapp.modules.simple.dom.solicitud.simple.Solicitud;
import domainapp.modules.simple.dom.solicitud.simple.SolicitudServices;


@Named(SimpleModule.NAMESPACE + ".SolicitudEquipoServices")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "SolicituEquipo")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SolicitudEquipoServices {

    @Inject @NotPersistent JugadorServices jugadorServices;
    @Inject @NotPersistent PartidoServices partidoServices;
    @Inject @NotPersistent RepositoryService repositoryService;
    @Inject EquipoServices equipoServices;
    @Inject SolicitudServices solicitudServices;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public String crearSolicitudEquipo(final String diaString, final String telefono, final String horarioSting) {


        Equipo equipo = equipoServices.buscarEquipo(telefono);

        if (equipo != null && partidoServices.hayPartido(telefono).isEmpty()) {

            Horarios horario = Horarios.valueOf(horarioSting);
            LocalDate dia = LocalDate.parse(diaString);
            Double precio = 0.0;
            NumeroCancha numeroCancha = partidoServices.definirCancha(diaString, horarioSting);
            SolicitudEquipo solicitudEquipo = haySolicitud(horario, dia, numeroCancha, precio);

            if (esEquipo1(solicitudEquipo)) {
                return asignarEquipo1YSolicitarPartido(solicitudEquipo, telefono);
            } else {
                return asignarEquipo2YSolicitarPartido(solicitudEquipo, telefono, horarioSting, diaString, precio);

            }

        } else if (equipo == null){
            throw new IllegalArgumentException("No existe equipo para este usuario, porfavor cree su equipo");
                }else throw new IllegalArgumentException("Ya tienes un partido");
    }

    private String asignarEquipo1YSolicitarPartido(SolicitudEquipo solicitudEquipo, String telefono) {
        Equipo equipo1 = equipoServices.buscarEquipo(telefono);
        solicitudEquipo.setEquipo1(equipo1);
        repositoryService.persist(solicitudEquipo);
        return "Se creo la Solicitud";
    }

    private String asignarEquipo2YSolicitarPartido(SolicitudEquipo solicitudEquipo, String telefono, String horarioSting, String diaString, Double precio) {
        Equipo equipo2 = equipoServices.buscarEquipo(telefono);
        if (esMismoEquipo(solicitudEquipo, equipo2)) {
            throw new IllegalArgumentException("El equipo no puede ser el mismo que el Equipo 1 en la solicitud actual");
        } else {
            solicitudEquipo.setEquipo2(equipo2);
            partidoServices.crearPartidoEquipos(horarioSting, diaString, solicitudEquipo.getEquipo1(),solicitudEquipo.getEquipo2(), precio);
            String mail1 = solicitudEquipo.getEquipo1().getRepresentante().getMail();
            String mail2 = solicitudEquipo.getEquipo2().getRepresentante().getMail();
            repositoryService.removeAndFlush(solicitudEquipo);
           return "{ \"email1\": \"" + mail1 + "\", \"email2\": \"" + mail2 + "\"}";
        }
    }

    private boolean esMismoEquipo(SolicitudEquipo solicitudEquipo, Equipo equipo) {
        return solicitudEquipo.getEquipo1().equals(equipo);
    }


    public SolicitudEquipo haySolicitud(final Horarios horario, final LocalDate dia, final NumeroCancha numeroCancha, final Double precio) {
        Equipo equipo1 = new Equipo();
//        Equipo equipo2 = new Equipo();

        return repositoryService.uniqueMatch(
                Query.named(SolicitudEquipo.class, SolicitudEquipo.NAMED_QUERY__FIND_BY_NAME_EXACT)
                        .withParameter("horario", horario)
                        .withParameter("dia", dia)
                        .withParameter("numeroCancha", numeroCancha)
        ).orElse(
                SolicitudEquipo.crearSolicitudEquipo(dia, horario, numeroCancha, precio, Estados.MATCHMAKING, equipo1,null)
        );
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<SolicitudEquipo> verSolicitudes() {
        return repositoryService.allInstances(SolicitudEquipo.class);
    }


    public boolean esEquipo1(SolicitudEquipo solicitudEquipo) {
        List<String> jugadoresEquipo = solicitudEquipo.getEquipo1().getJugadoresEquipo();
        if (jugadoresEquipo == null) {
            return true;
        } else {
            return false;
        }
    }

//    public Optional<SolicitudEquipo> buscarSolicitudPorEquipos(Equipo equipo1, Equipo equipo2){
//        return repositoryService.uniqueMatch(
//                Query.named(SolicitudEquipo.class, SolicitudEquipo.NAMED_QUERY__FIND_BY_EQUIPOS)
//                        .withParameter("equipo1", equipo1)
//                        .withParameter("equipo2", equipo2)
//        );
//    }


//    @Action
//    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
//    public boolean tieneSolicitud(String telefono) {
//        boolean tiene = false;
//        Equipo equipo1 = equipoServices.buscarEquipo(telefono);
//        Equipo equipo2 = equipoServices.buscarEquipo(telefono);
//
//        if (equipo1 != null) {
//            if (!repositoryService.uniqueMatch(Query.named(SolicitudEquipo.class, SolicitudEquipo.NAMED_QUERY__FIND_BY_EQUIPO1)
//                    .withParameter("equipo1", equipo1)
//            ).isEmpty()) {
//                tiene = true;
//            }
//        }
//
//        if (equipo2 != null) {
//            if (!repositoryService.uniqueMatch(Query.named(SolicitudEquipo.class, SolicitudEquipo.NAMED_QUERY__FIND_BY_EQUIPO2)
//                    .withParameter("equipo2", equipo2)
//            ).isEmpty()) {
//                tiene = true;
//            }
//        }
//
//        return tiene;
//    }


    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public boolean tieneSolicitud(String telefono) {
        List<SolicitudEquipo> solicitudesEquipo = verSolicitudes();

        for (SolicitudEquipo solicitud : solicitudesEquipo) {
            Equipo equipo1 = solicitud.getEquipo1();
            Equipo equipo2 = solicitud.getEquipo2();

            if ((equipo1 != null && equipo1.equals(equipoServices.buscarEquipo(telefono))) ||
                    (equipo2 != null && equipo2.equals(equipoServices.buscarEquipo(telefono)))) {
                return true; // El jugador está en un equipo que tiene una solicitud de equipo
            }
        }

        List<Solicitud> solicitudes = solicitudServices.verSolicitudes();

        for (Solicitud solicitud : solicitudes) {
            if (tieneJugadorEnSolicitud(solicitud, telefono)) {
                return true; // El jugador está en la lista de una solicitud normal
            }
        }

        return false;
    }

    private boolean tieneJugadorEnSolicitud(Solicitud solicitud, String telefono) {
        List<Jugador> jugadores = solicitud.getJugadores();

        for (Jugador jugador : jugadores) {
            if (jugador != null && jugador.getTelefono().equals(telefono)) {
                return true;
            }
        }

        return false;
    }




}

