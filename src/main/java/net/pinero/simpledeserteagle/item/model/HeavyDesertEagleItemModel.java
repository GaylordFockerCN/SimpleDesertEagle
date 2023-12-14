package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.HeavyDesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class HeavyDesertEagleItemModel extends GeoModel<HeavyDesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(HeavyDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(HeavyDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(HeavyDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/heavydeserteagle.png");
	}
}
