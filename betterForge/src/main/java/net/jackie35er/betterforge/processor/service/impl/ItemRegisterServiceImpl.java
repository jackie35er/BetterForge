package net.jackie35er.betterforge.processor.service.impl;

import com.google.common.base.CaseFormat;
import net.jackie35er.betterforge.annotation.BFItem;
import net.jackie35er.betterforge.processor.constants.ClassPaths;
import net.jackie35er.betterforge.processor.service.AnnotationService;
import net.jackie35er.betterforge.processor.service.ItemsService;
import net.jackie35er.betterforge.processor.service.ModIdService;
import net.jackie35er.betterforge.processor.service.ProcessingService;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class ItemRegisterServiceImpl extends ProcessingService implements ItemsService {

    private final AnnotationService annotationHandler;

    private final ModIdService modIdService;

    private final ClassPaths ITEM_REGISTER = ClassPaths.ITEM_REGISTER;

    public ItemRegisterServiceImpl(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnvironment) {
        super(roundEnvironment, processingEnvironment);
        annotationHandler = new AnnotationServiceImpl(roundEnvironment, processingEnvironment);
        modIdService = new ModIdServiceImpl(roundEnvironment, processingEnvironment);
    }

    public void generateItemRegister() {
        var file = createItemRegisterFile();
        try (var writer = new BufferedWriter(file.openWriter())) {
            writer.write(this.getPackageLine());
            writer.write(this.getImports());
            writer.write("public class ");
            writer.write(this.ITEM_REGISTER.getClassName());
            writer.write(" {\n");
            writer.write("\tprivate ");
            writer.write(this.ITEM_REGISTER.getClassName());
            writer.write("() {\n\t}\n\n");
            writer.write(this.getCreateDeferredRegisterLine());
            writer.write("\n");
            for(var line: this.getItemDeclaretionLines()){
                writer.write(line);
            }
            writer.write("\n");
            writer.write(this.getStaticRegisterBlock());
            writer.write("}");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private FileObject createItemRegisterFile() {
        try {
            return processingEnvironment.getFiler().createSourceFile(ITEM_REGISTER.value);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getPackageLine() {
        return String.format("package %s ;%n%n", ITEM_REGISTER.getPackageName());
    }
    private String getImports(){

        return getElementImports() +
                """
                import net.minecraft.world.item.Item;
                import net.minecraftforge.registries.DeferredRegister;
                import net.minecraftforge.registries.ForgeRegistries;
                import net.minecraftforge.registries.RegistryObject;
                import net.minecraftforge.eventbus.api.IEventBus;
                import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
                """;
    }

    private String getElementImports(){
        var elements = roundEnvironment.getElementsAnnotatedWith(BFItem.class);
        var bobTheBuilder = new StringBuilder();
        for (var element : elements) {
            bobTheBuilder.append("import ")
                    .append(processingEnvironment.getElementUtils().getPackageOf(element))
                    .append(".")
                    .append(element.getSimpleName().toString())
                    .append(";\n");
        }
        return bobTheBuilder.toString();
    }

    private String getStaticRegisterBlock(){
        return """
                    public static void register(IEventBus eventBus){
                        ITEMS.register(eventBus);
                    }
                """;
    }

    private String getCreateDeferredRegisterLine() {
        return String.format("\tprivate static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, \"%s\"); %n", modIdService.getModId());
    }


    private List<String> getItemDeclaretionLines() {
        var elements = roundEnvironment.getElementsAnnotatedWith(BFItem.class);
        List<String> lines = new ArrayList<>();
        for (var element : elements) {
            if (!checkForItemInheritence(element)) {
                processingEnvironment.getMessager()
                        .printMessage(
                                Diagnostic.Kind.WARNING,
                                String.format("Element annoteted with @Item does not inherit from %s. Skipping: %s", ClassPaths.FORGE_ITEM.value, element)
                        );
                continue;
            }
            String line = "\tpublic static final RegistryObject<Item> %s = ITEMS.register(\"%s\", () -> new %s(new Item.Properties())); %n";
            lines.add(
                    String.format(line,getItemDeclarationName(element),getItemId(element),element.getSimpleName().toString())
            );
        }

        return lines;
    }

    private String getItemId(Element element) {
        var annotation = element.getAnnotation(BFItem.class);
        if(annotation.value().equals("")){
            var caseFormatConverter = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
            return caseFormatConverter.convert(element.getSimpleName().toString());
        }
        return annotation.value();
    }

    private String getItemDeclarationName(Element element){
        var annotation = element.getAnnotation(BFItem.class);
        if(annotation.value().equals("")){
            var caseFormatConverter = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);
            return caseFormatConverter.convert(element.getSimpleName().toString());
        }

        return annotation.value().toUpperCase(Locale.ROOT);
    }

    private boolean checkForItemInheritence(Element element) {
        return processingEnvironment.getTypeUtils().isAssignable(
                processingEnvironment.getTypeUtils().erasure(element.asType()),
                processingEnvironment.getTypeUtils().erasure(processingEnvironment.getElementUtils().getTypeElement(ClassPaths.FORGE_ITEM.value).asType())
        );
    }

}
