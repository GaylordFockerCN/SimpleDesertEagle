package net.pinero.simpledeserteagle.item.model;

import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.DesertEagleItem;

import net.minecraft.resources.ResourceLocation;

public class DesertEagleItemModel extends GeoModel<DesertEagleItem> {

	private String textureResourceLocation = "";
	public DesertEagleItemModel(String textureResourceLocation){
		this.textureResourceLocation = textureResourceLocation;
	}

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
		//System.out.println("location"+textureResourceLocation);
		return new ResourceLocation("simpledeserteagle", textureResourceLocation);
	}
}
