package domainapp.modules.simple.dom.reportes;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


@DomainService(nature = NatureOfService.VIEW)
@Named(SimpleModule.NAMESPACE + ".ReporteJugador")
@DomainServiceLayout(named = "Reporte")
public class Reportes {

    @Inject JugadorServices jugadorServices;
    @Inject PartidoServices partidoServices;



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReporteJugadorPDF() throws JRException {


        List<Jugador> data = jugadorServices.verJugadores();

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

        return compilacionYCarga("JugadoresReporte.jrxml", ds, "Jugador");

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPDF() throws JRException {


        List<Partido> partidos = partidoServices.verPartidos();

        List<PartidosReporte> data = new ArrayList<>();

        for (Partido partido : partidos) {
            String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
            PartidosReporte partidosReporte1 = new PartidosReporte();
            partidosReporte1.setHorario(partido.getHorario().toString());
            partidosReporte1.setDia(partido.getDia().toString());
            partidosReporte1.setNumeroCancha(partido.getNumeroCancha().toString());
            partidosReporte1.setPrecio(partido.getPrecio());
            partidosReporte1.setTelefono(representante);
            partidosReporte1.setEstados(partido.getEstados().toString());
            data.add(partidosReporte1);
        }

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

        return compilacionYCarga("PartidosReporte.jrxml", ds, "Partido");

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPorEstadoPDF(Estados estados) throws JRException {


        List<Partido> partidos = partidoServices.verPartidos();

        List<PartidosReporte> data = new ArrayList<>();

        for (Partido partido : partidos) {
            if (partido.getEstados().equals(estados)) {
                String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
                PartidosReporte partidosReporte1 = new PartidosReporte();
                partidosReporte1.setHorario(partido.getHorario().toString());
                partidosReporte1.setDia(partido.getDia().toString());
                partidosReporte1.setNumeroCancha(partido.getNumeroCancha().toString());
                partidosReporte1.setPrecio(partido.getPrecio());
                partidosReporte1.setTelefono(representante);
                partidosReporte1.setEstados(partido.getEstados().toString());
                data.add(partidosReporte1);
            }
        }

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

        return compilacionYCarga("PartidosReporte.jrxml", ds, "Partidos Por Estado");

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Blob generarReportePartidosPorJugadorPDF(String telefono) throws JRException {



        List<Partido> partidos = partidoServices.verPartidos();

        List<PartidosReporte> data = new ArrayList<>();

        for (Partido partido : partidos) {
            if (partido.getRepresentante() != null) {
                if (partido.getRepresentante().getTelefono().equals(telefono)) {
                    String representante = partido.getRepresentante() == null ? "Sin Representante" : partido.getRepresentante().getTelefono().toString();
                    PartidosReporte partidosReporte1 = new PartidosReporte();
                    partidosReporte1.setHorario(partido.getHorario().toString());
                    partidosReporte1.setDia(partido.getDia().toString());
                    partidosReporte1.setNumeroCancha(partido.getNumeroCancha().toString());
                    partidosReporte1.setPrecio(partido.getPrecio());
                    partidosReporte1.setTelefono(representante);
                    partidosReporte1.setEstados(partido.getEstados().toString());
                    data.add(partidosReporte1);
                }
            }

        }

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);

        return compilacionYCarga("PartidosReporte.jrxml", ds, "Partidos Por Jugador " + telefono);
    }

    public Blob compilacionYCarga( String archivoDesing, JRBeanCollectionDataSource ds, String nombre) throws JRException {


        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(archivoDesing);
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport archivoCompilado = JasperCompileManager.compileReport(jasperDesign);


        JasperPrint JasperPrint = JasperFillManager.fillReport(archivoCompilado, null, ds);


        byte[] reportBytes = JasperExportManager.exportReportToPdf(JasperPrint);
        return new Blob("Reporte " + nombre + ".pdf", "application/pdf", reportBytes);
    }

}
