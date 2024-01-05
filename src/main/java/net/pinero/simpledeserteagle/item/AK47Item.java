package net.pinero.simpledeserteagle.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;
import net.pinero.simpledeserteagle.item.renderer.AK47Renderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class AK47Item extends DesertEagleItem{
    public  AK47Item() {
        super();
        fireDamage = (float) 0.45;
        coolDownTick = 1;
        ammoType = SimpledeserteagleModItems.DESERT_EAGLE_AMMO;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new AK47Renderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "Fire", 0, state -> PlayState.STOP)
                .triggerableAnim("fire", RawAnimation.begin().thenPlay("animation.ak47.fire")));
        data.add(new AnimationController<>(this, "Reload", 0, state -> PlayState.STOP)
                .triggerableAnim("reload", RawAnimation.begin().thenPlay("animation.ak47.reload")));

    }
}
