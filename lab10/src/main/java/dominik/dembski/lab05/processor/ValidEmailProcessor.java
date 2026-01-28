package dominik.dembski.lab05.processor;

import dominik.dembski.lab05.annotation.ValidEmail;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("dominik.dembski.lab05.annotation.ValidEmail")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class ValidEmailProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            
            for (Element element : annotatedElements) {
                ValidEmail validEmail = element.getAnnotation(ValidEmail.class);
                
                if (element.getKind() == ElementKind.FIELD) {
                    VariableElement var = (VariableElement) element;
                    processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "Processing @ValidEmail on field: " + var.getSimpleName()
                    );
                }
            }
        }
        return true;
    }
}
