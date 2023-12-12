
package net.pinero.simpledeserteagle.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;
import net.pinero.simpledeserteagle.item.renderer.DesertEagleBlazeItemRenderer;
import software.bernie.geckolib.animatable.GeoItem;


import java.util.function.Consumer;

public class DesertEagleBlazeItem extends FatherDesertEagleItem implements GeoItem {

	public DesertEagleBlazeItem() {
		super();
		fireDamage = (float) 1.35;
		coolDownTick = 10;
		ammoType = SimpledeserteagleModItems.DESERT_EAGLE_AMMO_PLUS;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new DesertEagleBlazeItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

}
