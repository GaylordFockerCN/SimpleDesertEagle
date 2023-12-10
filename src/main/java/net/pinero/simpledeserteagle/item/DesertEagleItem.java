
package net.pinero.simpledeserteagle.item;

import net.pinero.simpledeserteagle.item.renderer.DesertEagleItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;
public class DesertEagleItem extends FatherDesertEagleItem implements GeoItem {

	public DesertEagleItem() {
		super();
		damage = (float) 0.45;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new DesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

}
