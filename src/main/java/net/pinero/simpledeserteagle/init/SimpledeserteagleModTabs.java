
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.pinero.simpledeserteagle.init;

import net.pinero.simpledeserteagle.SimpledeserteagleMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class SimpledeserteagleModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimpledeserteagleMod.MODID);
	public static final RegistryObject<CreativeModeTab> SIMPLE_DESERT_EAGLE = REGISTRY.register("simple_desert_eagle",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.simpledeserteagle.simple_desert_eagle")).icon(() -> new ItemStack(SimpledeserteagleModItems.DESERT_EAGLE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(SimpledeserteagleModItems.DESERT_EAGLE_BULLET.get());
				tabData.accept(SimpledeserteagleModItems.DESERT_EAGLE_AMMO.get());
				tabData.accept(SimpledeserteagleModItems.DESERT_EAGLE_AMMO_PLUS.get());
				tabData.accept(SimpledeserteagleModItems.DESERT_EAGLE.get());
				tabData.accept(SimpledeserteagleModItems.COPPER_DESERT_EAGLE.get());
				tabData.accept(SimpledeserteagleModItems.DESERT_EAGLE_BLAZE.get());
				tabData.accept(SimpledeserteagleModItems.DIAMOND_DESERT_EAGLE.get());
				tabData.accept(SimpledeserteagleModItems.GOLDEN_DESERT_EAGLE.get());
				tabData.accept(SimpledeserteagleModItems.HEAVY_DESERT_EAGLE.get());
				tabData.accept(SimpledeserteagleModItems.AK47.get());
			})

					.build());
}
