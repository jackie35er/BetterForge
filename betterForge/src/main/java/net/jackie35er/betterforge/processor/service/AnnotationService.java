package net.jackie35er.betterforge.processor.service;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface AnnotationService {

    <T extends Annotation> Collection<T> getAnnotations(Class<T> tClass);
}
