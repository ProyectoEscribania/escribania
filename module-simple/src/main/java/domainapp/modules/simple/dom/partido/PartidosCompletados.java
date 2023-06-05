package domainapp.modules.simple.dom.partido;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;

import java.util.List;

@Collection
@CollectionLayout(defaultView = "table")

public class PartidosCompletados {

    private List<Partido> partidosCompletados;

    public PartidosCompletados(List<Partido> partidosCompletados) {
        this.partidosCompletados = partidosCompletados;
    }


    public List<Partido> ver(){return partidosCompletados;}



}
