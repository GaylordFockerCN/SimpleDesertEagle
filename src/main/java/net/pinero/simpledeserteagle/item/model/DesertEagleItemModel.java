package net.pinero.simpledeserteagle.item.model;

import net.pinero.simpledeserteagle.item.DesertEagleItem;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

public class DesertEagleItemModel extends GeoModel<DesertEagleItem> {
	@Override
	public ResourceLocation getAnimationResource(DesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "animations/deserteagle.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(DesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "geo/deserteagle.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DesertEagleItem animatable) {
		return new ResourceLocation("simpledeserteagle", "textures/item/texturecrc.png");
	}
}
