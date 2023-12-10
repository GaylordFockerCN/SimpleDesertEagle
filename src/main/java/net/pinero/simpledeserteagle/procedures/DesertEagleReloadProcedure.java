package net.pinero.simpledeserteagle.procedures;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.pinero.simpledeserteagle.item.FatherDesertEagleItem;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ProjectileWeaponItem;
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

	public static int totalNeed = 0;
	private static void procedure(FatherDesertEagleItem handItem,Entity entity,LevelAccessor world,boolean isMainHand) {
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		//延迟实现换弹逻辑，等动画和音效放完
		new Thread(() -> {

			try {
				int need = handItem.need();
				Item ammo = handItem.getAmmoType().get();
				Player player = (ServerPlayer) entity;//???妈的为什么改成Player就会一开火子弹就复原。。。搞不懂底层。。
				ItemStack stack = ProjectileWeaponItem.getHeldProjectile(player, e -> e.getItem() == ammo);

				boolean canFind = false;
				if (stack == ItemStack.EMPTY) {
					totalNeed = 0;
					canFind = searchItem(player,ammo,need,handItem,world);
				}

				if(canFind){
					handItem.reload(totalNeed);
					//播放动画
					if (world instanceof ServerLevel serverLevel) {
						//防止开火时换弹
						if(player.getCooldowns().isOnCooldown(handItem))return;
						handItem.reloadAnim(serverLevel, player, isMainHand?player.getMainHandItem():player.getOffhandItem());
					}
					//播放音效
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simpledeserteagle:deserteagelcrcreload")), SoundSource.PLAYERS, 1, 1);
						} else {
//							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simpledeserteagle:deserteagelcrcreload")), SoundSource.PLAYERS, 1, 1, false);
						}
					}
					Thread.sleep(FatherDesertEagleItem.RELOAD_TIME);
					if (world instanceof ServerLevel _level){
						Item anotherHandItem = ((Player)entity).getItemInHand(isMainHand?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND).getItem();

						String content = (isMainHand?"Main Hand Ammo:":" Off Hand Ammo:") +handItem.getAmmo() + "/" + FatherDesertEagleItem.MAX_AMMO;

						if(anotherHandItem instanceof FatherDesertEagleItem offHandItem && offHandItem.getClass()!=handItem.getClass()){//如果左右手相同会有很多bug..
							content = "Off Hand Ammo:"+ ( isMainHand?offHandItem.getAmmo():(handItem.getAmmo()) )+ "/"+FatherDesertEagleItem.MAX_AMMO+
									"      Main Hand Ammo:"+( isMainHand?handItem.getAmmo():(offHandItem.getAmmo()) )+ "/"+FatherDesertEagleItem.MAX_AMMO;
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
	private static boolean searchItem(Player player, Item ammo,int need,FatherDesertEagleItem handItem,LevelAccessor world){
		ItemStack stack = ItemStack.EMPTY;
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack teststack = player.getInventory().items.get(i);
			if (teststack != null && teststack.getItem() == ammo ) {
				if( teststack ==player.getItemInHand(InteractionHand.MAIN_HAND) ||  teststack == player.getItemInHand(InteractionHand.OFF_HAND)){
					if (world instanceof ServerLevel _level)
	 					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(player.getX(), player.getY(), player.getZ()), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"title @p actionbar \"Don't put ammo in hand!\"");
					continue;
				}
				stack = teststack;
				break;
			}
		}

		if (stack != ItemStack.EMPTY) {
			if (stack.getCount() >= need) {
				stack.shrink(need);
				totalNeed+=need;

			} else {
				int cnt = stack.getCount();
				stack.shrink(cnt);
				totalNeed+=cnt;
				searchItem(player,ammo,need - cnt,handItem,world);
			}
			return true;
		}else{
			return false;
		}
	}
	public static void execute(LevelAccessor world, Entity entity ) {

		if (entity == null || world.isClientSide())
			return;
		if(entity instanceof ServerPlayer player){//防止还没开火就换弹导致空指针异常（获取player默认在use的时候）
			for (int i = 0; i < player.getInventory().items.size(); i++) {
				ItemStack teststack = player.getInventory().items.get(i);
				if (teststack != null && teststack.getItem() instanceof FatherDesertEagleItem item) {
					item.player = player;
					//item.reload(teststack.getOrCreateTag().getInt(FatherDesertEagleItem.AMMO_TAG_NAME));
				}
			}
		}
		//右手持枪
		if((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() instanceof FatherDesertEagleItem mainHandItem
				&&!mainHandItem.isReloading() && !mainHandItem.isFull()) {
			procedure(mainHandItem,entity,world,true);
		}

		//TODO:两把一样的状态下似乎不管用qwq
		if((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() instanceof FatherDesertEagleItem offHandItem
				&&!offHandItem.isReloading() && !offHandItem.isFull()){
			procedure(offHandItem,entity,world,false);
		}

	}
}
