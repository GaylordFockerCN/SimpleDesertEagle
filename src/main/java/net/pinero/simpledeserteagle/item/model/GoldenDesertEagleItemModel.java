package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.GoldenDesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class GoldenDesertEagleItemModel extends GeoModel<GoldenDesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(GoldenDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GoldenDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GoldenDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/goldendeserteagle.png");
	}
}
