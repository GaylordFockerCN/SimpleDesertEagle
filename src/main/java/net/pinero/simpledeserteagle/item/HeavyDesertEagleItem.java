
package net.pinero.simpledeserteagle.item;

import software.bernie.geckolib.animatable.GeoItem;

import net.pinero.simpledeserteagle.item.renderer.HeavyDesertEagleItemRenderer;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;

public class HeavyDesertEagleItem extends FatherDesertEagleItem implements GeoItem {

	public HeavyDesertEagleItem() {
		super();
		fireDamage = (float) 0.625;
		coolDownTick = 20;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new HeavyDesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

}
