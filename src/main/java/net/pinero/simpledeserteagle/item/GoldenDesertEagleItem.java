
package net.pinero.simpledeserteagle.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import net.pinero.simpledeserteagle.item.renderer.GoldenDesertEagleItemRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import java.util.function.Consumer;

public class GoldenDesertEagleItem extends DesertEagleBlazeItem implements GeoItem {
	public String animationprocedure = "empty";

	public GoldenDesertEagleItem() {
		super();
		fireDamage = (float) 1.0;
		coolDownTick = 6;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new GoldenDesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}
}
