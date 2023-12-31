package net.pinero.simpledeserteagle.procedures;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.targets.FMLClientLaunchHandler;
import net.pinero.simpledeserteagle.item.DesertEagleItem;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
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
				ItemStack bullet = DesertEagleItem.getBulletItemStack(handItemStake,0);
				if(bullet.isEmpty()){
					need = DesertEagleItem.MAX_AMMO;
				}else need = bullet.getDamageValue();
				Player player = (Player)entity;
				int total = searchItem(player,ammo,need);
				if(total>0){
					DesertEagleItem handItem = (DesertEagleItem) handItemStake.getItem();
					handItem.isReloading = true;//限制同时换弹
					//handItemStake.getOrCreateTag().putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,false);
					//播放动画
					if (world instanceof ServerLevel serverLevel) {
						//防止开火时换弹
						if(player.getCooldowns().isOnCooldown(handItemStake.getItem()))return;
						//播放动画
						((DesertEagleItem)handItemStake.getItem()).reloadAnim(serverLevel, player, handItemStake);
						//播放音效
						serverLevel.playSound(player, x,y,z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simpledeserteagle:deserteagelcrcreload")), SoundSource.HOSTILE, 1, 1);

					}

					Thread.sleep(DesertEagleItem.RELOAD_TIME);
					//handItemStake.setDamageValue(need - total);

					ItemStack newBullet = handItemStake.copy();
					handItemStake.setDamageValue(0);//修复版本更新导致的物品出现磨损的问题
					newBullet.setCount(1);
					newBullet.setDamageValue(need - total);
					DesertEagleItem.setBulletItemStack(handItemStake,newBullet,0);

					//handItemStake.getOrCreateTag().putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,true);
					handItem.isReloading = false;
					//显示子弹数量信息
					if (world instanceof ServerLevel _level){
						ItemStack anotherHandItemStake = player.getItemInHand(isMainHand?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND);
						String content = (isMainHand? I18n.get("tips.simpledeserteagle.main_hand_ammo"):" "+I18n.get("tips.simpledeserteagle.off_hand_ammo")) +getBulletCount(handItemStake)+ "/" + DesertEagleItem.MAX_AMMO;

						if(anotherHandItemStake.getItem() instanceof DesertEagleItem){
							content = I18n.get("tips.simpledeserteagle.off_hand_ammo")+ ( isMainHand?getBulletCount(anotherHandItemStake):getBulletCount(handItemStake)) + "/"+ DesertEagleItem.MAX_AMMO+
									"      "+I18n.get("tips.simpledeserteagle.main_hand_ammo")+( isMainHand?getBulletCount(handItemStake):getBulletCount(anotherHandItemStake) )+ "/"+ DesertEagleItem.MAX_AMMO;
						}

						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"" +content+"\"");

					}
				}else{
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.no_ammo")+"\"");
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
			if(mainHandItem.getItem() instanceof DesertEagleItem item /*mainHandItem.getOrCreateTag().getBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG)*/){
				if(isFull(mainHandItem)){
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.main_ammo_full")+"\"");
				} else if (item.isReloading) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.reloading")+"\"");
				}else procedure(_livEnt.getMainHandItem(),entity,world,item.getAmmoType().get(),true);
			}
			if(offhandItem.getItem() instanceof DesertEagleItem item && /*offhandItem.getOrCreateTag().getBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG)*/!item.isReloading  && !isFull(offhandItem)){
				if(isFull(offhandItem)){
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.off_ammo_full")+"\"");
				} else if (item.isReloading) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.reloading")+"\"");
				}else procedure(_livEnt.getOffhandItem(),entity,world,item.getAmmoType().get(),false);
			}
		}

	}

	private static int getBulletCount(ItemStack stack){
		if(stack.getItem() instanceof DesertEagleItem){
			ItemStack bullet = DesertEagleItem.getBulletItemStack(stack,0);
			return bullet.getMaxDamage()-bullet.getDamageValue();
		}
		return 0;
	}

	private static boolean isFull(ItemStack gun){
		ItemStack bullet = DesertEagleItem.getBulletItemStack(gun,0);
		if(bullet.isEmpty())return false;
		return bullet.getDamageValue()==0;
	}
}
