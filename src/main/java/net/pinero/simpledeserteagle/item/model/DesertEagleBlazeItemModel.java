package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.DesertEagleBlazeItem;

import net.minecraft.resources.ResourceLocation;

public class DesertEagleBlazeItemModel extends GeoModel<DesertEagleBlazeItem> {
	@Override
	public ResourceLocation getAnimationResource(DesertEagleBlazeItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DesertEagleBlazeItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DesertEagleBlazeItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/deserteagle-blaze.png");
	}
}
