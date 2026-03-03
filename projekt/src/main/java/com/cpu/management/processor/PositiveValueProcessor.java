package com.cpu.management.processor;

import com.cpu.management.annotation.PositiveValue;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("com.cpu.management.annotation.PositiveValue")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PositiveValueProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            
            for (Element element : annotatedElements) {
                if (element.getKind() == ElementKind.FIELD) {
                    VariableElement field = (VariableElement) element;
                    TypeMirror fieldType = field.asType();
                    
                    // Sprawdź czy pole jest typu numerycznego
                    if (!isNumericType(fieldType)) {
                        processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            "@PositiveValue can only be applied to numeric fields (int, long, double, float). Field '" 
                                + field.getSimpleName() + "' is of type " + fieldType,
                            element
                        );
                    } else {
                        processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.NOTE,
                            "@PositiveValue applied to field: " + field.getSimpleName() + " (type: " + fieldType + ")",
                            element
                        );
                    }
                }
            }
        }
        return true;
    }
    
    private boolean isNumericType(TypeMirror type) {
        TypeKind kind = type.getKind();
        return kind == TypeKind.INT || kind == TypeKind.LONG || 
               kind == TypeKind.DOUBLE || kind == TypeKind.FLOAT ||
               kind == TypeKind.SHORT || kind == TypeKind.BYTE;
    }
}
