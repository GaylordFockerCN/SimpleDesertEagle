
package net.pinero.simpledeserteagle.item;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoItem;

import net.pinero.simpledeserteagle.item.renderer.DiamondDesertEagleItemRenderer;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Item;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;

public class DiamondDesertEagleItem extends DesertEagleBlazeItem implements GeoItem {
	public DiamondDesertEagleItem() {
		super();
		coolDownTick = 8;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new DiamondDesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

}
