package net.jackie35er.betterforge.processor;

import net.jackie35er.betterforge.processor.service.AnnotationService;
import net.jackie35er.betterforge.processor.service.ItemsService;
import net.jackie35er.betterforge.processor.service.ModIdService;
import net.jackie35er.betterforge.processor.service.impl.AnnotationServiceImpl;
import net.jackie35er.betterforge.processor.service.impl.ItemRegisterServiceImpl;
import net.jackie35er.betterforge.processor.service.impl.ModIdServiceImpl;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes(
        {
                "net.minecraftforge.fml.common.Mod",
                "net.jackie35er.betterforge.annotation.Item"
        }
)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ModProccessor extends AbstractProcessor {

    private ModIdService modIdHandler;
    private AnnotationService annotationHandler;
    private ItemsService itemsService;


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        initHandlers(roundEnv, this.processingEnv);
        if(annotations.isEmpty())
            return false;
        System.out.println(roundEnv.getElementsAnnotatedWith(Mod.class));
        System.out.println(modIdHandler.getModId());
        itemsService.generateItemRegister();



        return true;
    }


    private void initHandlers(RoundEnvironment roundEnv, ProcessingEnvironment processingEnvironment){
        this.modIdHandler = new ModIdServiceImpl(roundEnv, processingEnvironment);
        this.annotationHandler = new AnnotationServiceImpl(roundEnv, processingEnvironment);
        this.itemsService  = new ItemRegisterServiceImpl(roundEnv,processingEnvironment);
    }

}
