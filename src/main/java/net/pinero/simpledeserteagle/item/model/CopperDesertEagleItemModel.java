package net.pinero.simpledeserteagle.item.model;

import net.pinero.simpledeserteagle.item.DesertEagleBlazeItem;
import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.CopperDesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class CopperDesertEagleItemModel extends GeoModel<CopperDesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(CopperDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CopperDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CopperDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/copperdeserteagle.png");
	}
}
