package net.pinero.simpledeserteagle.item;

import net.minecraft.world.item.*;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;
import net.pinero.simpledeserteagle.entity.DesertEagleBulletEntity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;

public class DesertEagleBulletItem extends Item {
    public DesertEagleBulletItem() {
        super(new Item.Properties().stacksTo(64));
    }

}