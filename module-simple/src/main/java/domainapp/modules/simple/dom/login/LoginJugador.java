package domainapp.modules.simple.dom.login;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.encargado.EncargadoServices;
import domainapp.modules.simple.dom.jugador.JugadorServices;

@Named(SimpleModule.NAMESPACE + ".LoginJugador")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "LoginJugador")
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class LoginJugador {

    @Inject JugadorServices jugadorServices;
    @Inject EncargadoServices encargadoServices;

    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public boolean LoginJugador(final String telefono,final String password){
        if (jugadorServices.buscarJugador(telefono)!=null && jugadorServices.buscarJugador(telefono).getPassword().equals(password)){
            return true;
        }

        else{
            return false;
        }

    }
}
