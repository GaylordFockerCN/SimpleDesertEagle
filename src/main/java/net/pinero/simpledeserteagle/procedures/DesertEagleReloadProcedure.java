package net.pinero.simpledeserteagle.procedures;

import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.pinero.simpledeserteagle.item.FatherDesertEagleItem;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class DesertEagleReloadProcedure {

	private static void procedure(ItemStack handItemStake,Entity entity,LevelAccessor world,Item ammo,boolean isMainHand) {
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		//延迟实现换弹逻辑，等动画和音效放完
		new Thread(() -> {//防止sleep卡死

			try {
				int need = 0;
				ItemStack bullet = FatherDesertEagleItem.getBulletItemStack(handItemStake,0);
				if(bullet.isEmpty()){
					need = FatherDesertEagleItem.MAX_AMMO;
				}else need = bullet.getDamageValue();
				Player player = (Player)entity;
				int total = searchItem(player,ammo,need);
				if(total>0){
					FatherDesertEagleItem handItem = (FatherDesertEagleItem) handItemStake.getItem();
					handItem.isReloading = true;//限制同时换弹
					//handItemStake.getOrCreateTag().putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,false);
					//播放动画
					if (world instanceof ServerLevel serverLevel) {
						//防止开火时换弹
						if(player.getCooldowns().isOnCooldown(handItemStake.getItem()))return;
						//播放动画
						((FatherDesertEagleItem)handItemStake.getItem()).reloadAnim(serverLevel, player, handItemStake);
						//播放音效
						serverLevel.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simpledeserteagle:deserteagelcrcreload")), SoundSource.HOSTILE, 1, 1);

					}

					Thread.sleep(FatherDesertEagleItem.RELOAD_TIME);
					//handItemStake.setDamageValue(need - total);

					ItemStack newBullet = handItemStake.copy();
					newBullet.setCount(1);
					//newBullet.setDamageValue(need - total);
					FatherDesertEagleItem.setBulletItemStack(handItemStake,newBullet,0);

					//handItemStake.getOrCreateTag().putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,true);
					handItem.isReloading = false;
					//显示子弹数量信息
					if (world instanceof ServerLevel _level){
						ItemStack anotherHandItemStake = player.getItemInHand(isMainHand?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND);

						String content = (isMainHand?"Main Hand Ammo:":" Off Hand Ammo:") +getBulletCount(handItemStake)+ "/" + FatherDesertEagleItem.MAX_AMMO;

						if(anotherHandItemStake.getItem() instanceof FatherDesertEagleItem offHandItem){
							content = "Off Hand Ammo:"+ ( isMainHand?getBulletCount(anotherHandItemStake):getBulletCount(handItemStake)) + "/"+FatherDesertEagleItem.MAX_AMMO+
									"      Main Hand Ammo:"+( isMainHand?getBulletCount(handItemStake):getBulletCount(anotherHandItemStake) )+ "/"+FatherDesertEagleItem.MAX_AMMO;
						}

						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"" +content+"\"");

					}
				}else{
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"No Ammo!\"");
				}
			} catch (Exception e) {//Exception高发地，实在不会搞
				throw new RuntimeException(e);
			}

		}).start();
	}

	//递归搜索物品栈
	private static int searchItem(Player player, Item ammo,int need){
		int total = 0;
		ItemStack stack = ItemStack.EMPTY;
		if(ammo == player.getMainHandItem().getItem()){
			stack = player.getMainHandItem();
		}else if(ammo == player.getOffhandItem().getItem()){
			stack = player.getOffhandItem();
		}else {
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				ItemStack teststack = player.getInventory().items.get(i);
				if (teststack != null && teststack.getItem() == ammo ) {
					stack = teststack;
					break;
				}
			}
		}

		if (stack != ItemStack.EMPTY) {
			if (stack.getCount() >= need) {
				stack.shrink(need);
				return need;
			} else {
				int cnt = stack.getCount();
				stack.shrink(cnt);
				total += cnt;
				total += searchItem(player,ammo,need - cnt);
				return total;
			}
		}else{
			return 0;
		}
	}
	public static void execute(LevelAccessor world, Entity entity ) {
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (entity == null)
			return;

		if(entity instanceof LivingEntity _livEnt){
			ItemStack mainHandItem = _livEnt.getMainHandItem();
			ItemStack offhandItem = _livEnt.getOffhandItem();
			if(mainHandItem.getItem() instanceof FatherDesertEagleItem item /*mainHandItem.getOrCreateTag().getBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG)*/){
				if(isFull(mainHandItem)){
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"Main hand ammo is FULL!\"");
				} else if (item.isReloading) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"Main hand is Reloading!\"");
				}else procedure(_livEnt.getMainHandItem(),entity,world,item.getAmmoType().get(),true);
			}
			if(offhandItem.getItem() instanceof FatherDesertEagleItem item && /*offhandItem.getOrCreateTag().getBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG)*/!item.isReloading  && !isFull(offhandItem)){
				if(isFull(offhandItem)){
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"Offhand ammo is FULL!\"");
				} else if (item.isReloading) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"Offhand is Reloading!\"");
				}else procedure(_livEnt.getOffhandItem(),entity,world,item.getAmmoType().get(),false);
			}
		}

	}

	private static int getBulletCount(ItemStack stack){
		if(stack.getItem() instanceof FatherDesertEagleItem){
			ItemStack bullet = FatherDesertEagleItem.getBulletItemStack(stack,0);
			return bullet.getMaxDamage()-bullet.getDamageValue();
		}
		return 0;
	}

	private static boolean isFull(ItemStack gun){
		ItemStack bullet = FatherDesertEagleItem.getBulletItemStack(gun,0);
		if(bullet.isEmpty())return false;
		return bullet.getDamageValue()==0;
	}
}
