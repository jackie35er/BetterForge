package net.jackie35er.tutorialmod.item;

import net.jackie35er.betterforge.annotation.BFItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

@BFItem
public class SomeItem extends Item {
    public SomeItem(Properties pProperties) {
        super(pProperties.tab(CreativeModeTab.TAB_MISC));
    }
}
