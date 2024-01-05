package net.pinero.simpledeserteagle.item.model;

import net.pinero.simpledeserteagle.item.AK47Item;
import software.bernie.geckolib.model.GeoModel;

import net.pinero.simpledeserteagle.item.DesertEagleBlazeItem;

import net.minecraft.resources.ResourceLocation;

public class AK47ItemModel extends GeoModel<AK47Item> {
    @Override
    public ResourceLocation getAnimationResource(AK47Item animatable) {
        return new ResourceLocation("simpledeserteagle", "animations/ak47.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(AK47Item animatable) {
        return new ResourceLocation("simpledeserteagle", "geo/ak47.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AK47Item animatable) {
        return new ResourceLocation("simpledeserteagle", "textures/item/ak47test.png");
    }
}
