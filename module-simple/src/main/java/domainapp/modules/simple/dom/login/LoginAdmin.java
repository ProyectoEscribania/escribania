package domainapp.modules.simple.dom.login;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.encargado.EncargadoServices;

import domainapp.modules.simple.dom.jugador.JugadorServices;

import lombok.RequiredArgsConstructor;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.LoginException;

@Named(SimpleModule.NAMESPACE + ".LoginAdmin")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout(named = "LoginAdmin")
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class LoginAdmin {


    @Inject EncargadoServices encargadoServices;

    @Action
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public boolean LoginAdmin(final String telefono,final String password) throws LoginException {
        if (encargadoServices.buscarEncargado(telefono)!=null && encargadoServices.buscarEncargado(telefono).getPassword().equals(password)){
            return true;
        }

        else{
            return false;
        }

    }
}