package net.jackie35er.betterforge.processor.service.impl;

import net.jackie35er.betterforge.processor.service.AnnotationService;
import net.jackie35er.betterforge.processor.service.ProcessingService;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

public class AnnotationServiceImpl extends ProcessingService implements AnnotationService {
    public AnnotationServiceImpl(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        super(roundEnvironment, processingEnvironment);
    }

    public <T extends Annotation> Collection<T> getAnnotations(Class<T> tClass){
        var elements = new ArrayList<>(this.roundEnvironment.getElementsAnnotatedWith(tClass));

        return elements.stream()
                .map(element -> element.getAnnotation(tClass))
                .toList();
    }


}
