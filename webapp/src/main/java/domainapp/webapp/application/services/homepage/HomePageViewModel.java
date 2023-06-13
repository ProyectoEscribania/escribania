package domainapp.webapp.application.services.homepage;


import javax.inject.Inject;
import javax.inject.Named;


import domainapp.modules.simple.dom.partido.Partido;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.HomePage;
import org.apache.causeway.applib.annotation.Nature;


import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.so.SimpleObjects;
import domainapp.modules.simple.dom.jugador.JugadorServices;



@Named(SimpleModule.NAMESPACE + ".HomePageViewModel")
@DomainObject(nature = Nature.VIEW_MODEL)
@HomePage
@DomainObjectLayout()

public class HomePageViewModel {



    @Inject JugadorServices jugadorServices;
    @Inject SimpleObjects simpleObjects;



}
