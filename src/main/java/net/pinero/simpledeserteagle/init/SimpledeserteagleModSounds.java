
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.pinero.simpledeserteagle.init;

import net.pinero.simpledeserteagle.SimpledeserteagleMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class SimpledeserteagleModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SimpledeserteagleMod.MODID);
	public static final RegistryObject<SoundEvent> DESERTEAGELCRCFIRE = REGISTRY.register("deserteagelcrcfire", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("simpledeserteagle", "deserteagelcrcfire")));
	public static final RegistryObject<SoundEvent> DESERTEAGELCRCRELOAD = REGISTRY.register("deserteagelcrcreload", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("simpledeserteagle", "deserteagelcrcreload")));
}
