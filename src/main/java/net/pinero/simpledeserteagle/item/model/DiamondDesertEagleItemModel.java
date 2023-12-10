package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.DiamondDesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class DiamondDesertEagleItemModel extends GeoModel<DiamondDesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(DiamondDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DiamondDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DiamondDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/diamonddeserteagle.png");
	}
}
