package net.jackie35er.betterforge.processor.service.impl;

import net.jackie35er.betterforge.processor.service.ModIdService;
import net.jackie35er.betterforge.processor.service.ProcessingService;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;

public class ModIdServiceImpl extends ProcessingService implements ModIdService {

    private static String modId = null;

    private final AnnotationServiceImpl annotationHandler;

    public ModIdServiceImpl(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        super(roundEnvironment, processingEnvironment);
        this.annotationHandler = new AnnotationServiceImpl(roundEnvironment,processingEnvironment);
    }

    @Override public String getModId() {
        if (modId != null)
            return modId;

        return extractModId();
    }

    private String extractModId() {
        var modAnnotation = getModAnnotation();

        return modAnnotation.value();
    }

    private Mod getModAnnotation() {
        List<Mod> mods = new ArrayList<>(annotationHandler.getAnnotations(Mod.class));
        if (mods.size() > 1) {
            processingEnvironment.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    String.format("Multiple Mod classes found. Make sure there is only one class annoteted with @Mod. Proceeding with the first one found: %s", mods.get(0))
            );
        }

        return mods.get(0);
    }
}
