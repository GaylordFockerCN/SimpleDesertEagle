
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.pinero.simpledeserteagle.init;

import net.pinero.simpledeserteagle.item.*;
import net.pinero.simpledeserteagle.SimpledeserteagleMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

public class SimpledeserteagleModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SimpledeserteagleMod.MODID);
	public static final RegistryObject<Item> DESERT_EAGLE_BULLET = REGISTRY.register("desert_eagle_bullet", () -> new DesertEagleBulletItem());
	public static final RegistryObject<Item> DESERT_EAGLE_AMMO = REGISTRY.register("desert_eagle_ammo", () -> new DesertEagleAmmoItem());
	public static final RegistryObject<Item> DESERT_EAGLE_AMMO_PLUS = REGISTRY.register("desert_eagle_ammo_plus", () -> new DesertEagleAmmoPlusItem());
	//public static final RegistryObject<Item> FATHER_DESERT_EAGLE = REGISTRY.register("father_desert_eagle", () -> new FatherDesertEagleItem());
	public static final RegistryObject<Item> DESERT_EAGLE = REGISTRY.register("desert_eagle", () -> new DesertEagleItem());
	//public static final RegistryObject<Item> COPPER_DESERT_EAGLE = REGISTRY.register("copper_desert_eagle", () -> new FatherDesertEagleItem("textures/item/copperdeserteagle.png"));
	public static final RegistryObject<Item> COPPER_DESERT_EAGLE = REGISTRY.register("copper_desert_eagle", () -> new CopperDesertEagleItem());
	//public static final RegistryObject<Item> DESERT_EAGLE_BLAZE = REGISTRY.register("desert_eagle_blaze", () -> new FatherDesertEagleItem("textures/item/deserteagle-blaze.png",(float) 1.35,10,16));
	public static final RegistryObject<Item> DESERT_EAGLE_BLAZE = REGISTRY.register("desert_eagle_blaze", () -> new DesertEagleBlazeItem());
	public static final RegistryObject<Item> DIAMOND_DESERT_EAGLE = REGISTRY.register("diamond_desert_eagle", () -> new DiamondDesertEagleItem());
	public static final RegistryObject<Item> GOLDEN_DESERT_EAGLE = REGISTRY.register("golden_desert_eagle", () -> new GoldenDesertEagleItem());
	public static final RegistryObject<Item> HEAVY_DESERT_EAGLE = REGISTRY.register("heavy_desert_eagle", () -> new HeavyDesertEagleItem());

	public static final RegistryObject<Item> AK47 = REGISTRY.register("ak47", () -> new AK47Item());
}
