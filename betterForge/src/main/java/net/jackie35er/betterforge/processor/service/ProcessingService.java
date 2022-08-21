package net.jackie35er.betterforge.processor.service;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

public abstract class ProcessingService {

    protected final RoundEnvironment roundEnvironment;
    protected final ProcessingEnvironment processingEnvironment;

    protected ProcessingService(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment ) {
        this.processingEnvironment = processingEnvironment;
        this.roundEnvironment = roundEnvironment;
    }
}
