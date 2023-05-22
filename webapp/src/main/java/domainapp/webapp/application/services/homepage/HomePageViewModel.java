package domainapp.webapp.application.services.homepage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.HomePage;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.TableDecorator;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.dom.so.SimpleObjects;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.partido.Partido;

import org.hibernate.validator.constraints.CodePointLength;

@Named(SimpleModule.NAMESPACE + ".HomePageViewModel")
@DomainObject(nature = Nature.VIEW_MODEL)
@HomePage
@DomainObjectLayout()
public class HomePageViewModel {



    @Inject JugadorServices jugadorServices;
    @Inject PartidoServices partidoServices;
    @Inject SimpleObjects simpleObjects;





    @Action
    @ActionLayout()
    public void crearJugador(String nombre, String apellido, String telefono, String mail) {
         jugadorServices.crearJugador(nombre,apellido,telefono,mail);
    }

//    @Collection
//    @CollectionLayout(tableDecorator = TableDecorator.DatatablesNet.class)
//    public List<Partido> Partidos() {
//        return partidoServices.verPartidos();
//    }

}
