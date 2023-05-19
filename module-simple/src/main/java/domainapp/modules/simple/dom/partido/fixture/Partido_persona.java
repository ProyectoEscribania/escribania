package domainapp.modules.simple.dom.partido.fixture;

import domainapp.modules.simple.dom.partido.Partido;
import domainapp.modules.simple.dom.partido.PartidoServices;
import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.dom.so.SimpleObjects;

import lombok.*;
import lombok.experimental.Accessors;

import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.applib.value.Blob;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import org.springframework.core.io.ClassPathResource;

import javax.inject.Inject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;

@RequiredArgsConstructor
public enum Partido_persona
implements Persona<Partido, Partido_persona.Builder> {;



    private final LocalDate horario;
    private final String contentFileName;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public Partido findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(PartidoServices.class).map(x -> x.buscarPartido(horario)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Partido> {

        @Getter @Setter private Partido_persona persona;

        @Override
        protected Partido buildResult(final ExecutionContext ec) {

            val simpleObject = wrap(partidoServices).crearPartido(persona.horario);

            if (persona.contentFileName != null) {
                val bytes = toBytes(persona.contentFileName);
                val attachment = new Blob(persona.contentFileName, "application/pdf", bytes);
                simpleObject.updateAttachment(attachment);
            }

            simpleObject.setLastCheckedIn(clockService.getClock().nowAsLocalDate().plusDays(fakeDataService.ints().between(-10, +10)));

            return simpleObject;
        }

        @SneakyThrows
        private byte[] toBytes(String fileName){
            InputStream inputStream = new ClassPathResource(fileName, getClass()).getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        }

        // -- DEPENDENCIES

        @Inject PartidoServices partidoServices;
        @Inject ClockService clockService;
        @Inject FakeDataService fakeDataService;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Partido, Partido_persona, Builder> {
        public PersistAll() {
            super(Partido_persona.class);
        }
    }


}
