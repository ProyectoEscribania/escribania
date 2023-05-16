package domainapp.modules.simple.dom.partido.types;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Property;

import javax.jdo.annotations.Column;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Column(length = Precio.MAX_LEN, allowsNull = "false")
@Property(maxLength = Precio.MAX_LEN)
@Parameter(maxLength = Precio.MAX_LEN)
@ParameterLayout(named = "Precio")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Precio {
    int MAX_LEN = 100000;
}
