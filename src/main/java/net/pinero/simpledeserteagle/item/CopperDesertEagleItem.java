
package net.pinero.simpledeserteagle.item;

import software.bernie.geckolib.animatable.GeoItem;
import net.pinero.simpledeserteagle.item.renderer.CopperDesertEagleItemRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import java.util.function.Consumer;

public class CopperDesertEagleItem extends DesertEagleItem implements GeoItem {
	public CopperDesertEagleItem() {
		super();
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new CopperDesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

}
