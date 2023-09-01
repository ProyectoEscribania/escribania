package domainapp.modules.simple.dom.reportes;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.value.Blob;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.jugador.Jugador;
import domainapp.modules.simple.dom.jugador.JugadorServices;
import domainapp.modules.simple.dom.partido.Partido;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.partido.types.Estados;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@DomainService(nature = NatureOfService.VIEW)
@Named(SimpleModule.NAMESPACE + ".ReporteJugador")
@DomainServiceLayout(named = "Reporte", menuBar = DomainServiceLayout.MenuBar.PRIMARY)
public class Reportes {

    @Inject JugadorServices jugadorServices;
    @Inject PartidoServices partidoServices;


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReporteJugadorPDF() throws JRException {

        String path = "C:\\Users\\lucak\\IdeaProjects\\CanchaApp\\module-simple\\src\\main\\java\\domainapp\\modules\\simple\\dom\\reportes\\JugadoresReporte.jrxml";

        List<Jugador> data = jugadorServices.verJugadores();

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

        return compilacionYCarga(path, null, ds);

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPDF() throws JRException {

        String path = "C:\\Users\\lucak\\IdeaProjects\\CanchaApp\\module-simple\\src\\main\\java\\domainapp\\modules\\simple\\dom\\reportes\\PartidosReporte.jrxml";

        List<Partido> data = partidoServices.verPartidos();

        Map<String, Object> parameters = new HashMap<>();

        for (Partido partido : data) {
            String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
            parameters.put("dia", partido.getDia().toString());
            parameters.put("horario", partido.getHorario().toString());
            parameters.put("numeroCancha", partido.getNumeroCancha().toString());
            parameters.put("precio", partido.getPrecio());
            parameters.put("estados", partido.getEstados().toString());
            parameters.put("telefono", representante);
        }

        return compilacionYCarga(path, parameters, null);

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPorEstadoPDF(Estados estados) throws JRException {

        String path = "C:\\Users\\lucak\\IdeaProjects\\CanchaApp\\module-simple\\src\\main\\java\\domainapp\\modules\\simple\\dom\\reportes\\PartidosReporte.jrxml";

        List<Partido> data = partidoServices.verPartidos();

        Map<String, Object> parameters = new HashMap<>();

        for (Partido partido : data) {
            if (partido.getEstados().equals(partido.getEstados().equals(estados))) {
                String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
                parameters.put("dia", partido.getDia().toString());
                parameters.put("horario", partido.getHorario().toString());
                parameters.put("numeroCancha", partido.getNumeroCancha().toString());
                parameters.put("precio", partido.getPrecio());
                parameters.put("estados", partido.getEstados().toString());
                parameters.put("telefono", representante);
            }
        }

        return compilacionYCarga(path, parameters, null);

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPorJugadorPDF(String telefono) throws JRException {


        String path = "C:\\Users\\lucak\\IdeaProjects\\CanchaApp\\module-simple\\src\\main\\java\\domainapp\\modules\\simple\\dom\\reportes\\PartidosReporte.jrxml";

        List<Partido> data = partidoServices.verPartidos();

        Map<String, Object> parameters = new HashMap<>();

        for (Partido partido : data) {
            if (partido.getRepresentante() != null) {
                if (partido.getEstados().equals(partido.getRepresentante().getTelefono().equals(telefono))) {
                    String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
                    parameters.put("dia", partido.getDia().toString());
                    parameters.put("horario", partido.getHorario().toString());
                    parameters.put("numeroCancha", partido.getNumeroCancha().toString());
                    parameters.put("precio", partido.getPrecio());
                    parameters.put("estados", partido.getEstados().toString());
                    parameters.put("telefono", representante);
                }
            }

        }

        return compilacionYCarga(path, parameters, null);
    }

    public Blob compilacionYCarga(String path, Map<String, Object> parameters, JRBeanCollectionDataSource ds) throws JRException {

        JasperReport reporteCompilado = JasperCompileManager.compileReport(path);

        JasperPrint JasperPrint = parameters == null ? JasperFillManager.fillReport(reporteCompilado, null, ds) : JasperFillManager.fillReport(reporteCompilado, parameters);


        byte[] reportBytes = JasperExportManager.exportReportToPdf(JasperPrint);
        return new Blob("Reporte.pdf", "application/pdf", reportBytes);
    }

}
