package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.FatherDesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class FatherDesertEagleItemModel extends GeoModel<FatherDesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(FatherDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FatherDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FatherDesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/texturecrc.png");
	}
}
