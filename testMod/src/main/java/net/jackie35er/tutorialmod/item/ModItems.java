package net.jackie35er.tutorialmod.item;

import net.jackie35er.tutorialmod.common.constants.ModConstants;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;


public class  ModItems {

    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MOD_ID);

    public static final RegistryObject<Item> CITRINE = ITEMS.register(
            "citrine",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC))
    );

    public static final RegistryObject<Item> RAW_CITRINE = ITEMS.register(
            "raw_citrine",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC))
    );


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
