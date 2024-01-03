package net.pinero.simpledeserteagle.procedures;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.pinero.simpledeserteagle.KeyMappingsTest;
import net.pinero.simpledeserteagle.item.DesertEagleItem;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModEntities;
import net.pinero.simpledeserteagle.entity.DesertEagleBulletEntity;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
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

import java.security.Key;

public class DesertEagleRightClickAirProcedure {
	public static void execute(LevelAccessor world, Entity entity, InteractionHand hand) {

		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (entity == null )
			return;

		if(entity instanceof LivingEntity _livEnt){
			ItemStack handItemStake = (hand == InteractionHand.MAIN_HAND?_livEnt.getMainHandItem():_livEnt.getOffhandItem());
			if(handItemStake.getItem() instanceof DesertEagleItem handItem){
				boolean isCooldown = false;
				if (entity instanceof Player _player){
					isCooldown = _player.getCooldowns().isOnCooldown(handItem);
				}

//				int bulletID = 0;
//				ItemStack bulletStack = ItemStack.EMPTY.copy();
//				for(; bulletID < FatherDesertEagleItem.numAmmoItemsInGun; bulletID++)
//				{
//					ItemStack checkingStack = FatherDesertEagleItem.getBulletItemStack(handItemStake, bulletID);
//					if(checkingStack != null && checkingStack.getDamageValue() < checkingStack.getMaxDamage())
//					{
//						bulletStack = checkingStack;
//						break;
//					}
//				}
				ItemStack bulletStack = DesertEagleItem.getBulletItemStack(handItemStake, 0);

				if (!handItem.isReloading && !bulletStack.isEmpty()&&bulletStack.getDamageValue() < bulletStack.getMaxDamage() &&!isCooldown) {

					if (entity instanceof Player _player && !_player.isCreative()){
						final ItemStack bullet = bulletStack;
						final Integer bulletID1 = 0;
						bullet.setDamageValue(bullet.getDamageValue() + 1);
						//Update the stack in the gun
						DesertEagleItem.setBulletItemStack(handItemStake, bullet, bulletID1);
					}

					if (world instanceof ServerLevel projectileLevel) {
						Projectile _entityToSpawn =	new Object() {
							public Projectile getArrow(Level level, Entity shooter, float damage, int knockBack, byte piercing) {
								AbstractArrow entityToSpawn = new DesertEagleBulletEntity(SimpledeserteagleModEntities.DESERT_EAGLE_BULLET.get(), level);
								entityToSpawn.setOwner(shooter);
								entityToSpawn.setNoGravity(true);
								entityToSpawn.setBaseDamage(damage);
								entityToSpawn.setKnockback(knockBack);
								entityToSpawn.setSilent(true);
								entityToSpawn.setPierceLevel(piercing);
								entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
								return entityToSpawn;
							}
						}.getArrow(projectileLevel, entity, handItem.getFireDamage(), 1, (byte) 5);
						_entityToSpawn.setPos(x, entity.getEyeY() - (double)0.15F, z);
						_entityToSpawn.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, handItem.getPower(), 0);
						projectileLevel.addFreshEntity(_entityToSpawn);


					}
					if (entity instanceof Player player && world instanceof ServerLevel serverLevel) {
						handItem.fireAnim(serverLevel, player, hand == InteractionHand.MAIN_HAND?player.getMainHandItem():player.getOffhandItem());

					}

					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(handItem, handItem.getCoolDownTick());

					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							//播放音效
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("simpledeserteagle:deserteagelcrcfire")), SoundSource.HOSTILE, 1, 1);
						} else {
							//实现抖动
							double[] recoilTimer = {0}; // 后坐力计时器
							double totalTime = 100;
							int sleepTime = 2;
							double recoilDuration = totalTime / sleepTime; // 后坐力持续时间
							float speed = (float) ((Math.random() * 2) - 1) / 10;
							Runnable recoilRunnable = () -> {
								//开始抖动(简单匀速运动，不够真实。。)
								while (recoilTimer[0] < recoilDuration) {
									// 逐渐调整玩家的视角
									float newPitch = entity.getXRot() - (float) 0.2;//实时获取，以防鼠标冲突
									float newYaw = entity.getYRot() - speed;
									entity.setYRot(newYaw);
									entity.setXRot(newPitch);
									entity.yRotO = entity.getYRot();
									entity.xRotO = entity.getXRot();
									recoilTimer[0]++; // 计时器递增
									try {
										Thread.sleep(sleepTime);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								//归位
								while (recoilTimer[0] > 0) {
									float newPitch = entity.getXRot() + (float) 0.2;
									float newYaw = entity.getYRot() + speed;
									entity.setXRot(newPitch);
									entity.setYRot(newYaw);
									entity.xRotO = entity.getXRot();
									entity.yRotO = entity.getYRot();
									recoilTimer[0]--; // 计时器递增
									try {
										Thread.sleep(sleepTime);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							};
							Thread recoilThread = new Thread(recoilRunnable);
							recoilThread.start();
						}
					}

					if (world instanceof ServerLevel _level){
						boolean isMainHand = hand == InteractionHand.MAIN_HAND;

						ItemStack anotherHandItemStake = _livEnt.getItemInHand(isMainHand?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND);
						String content = (isMainHand?I18n.get("tips.simpledeserteagle.main_hand_ammo"):" "+I18n.get("tips.simpledeserteagle.off_hand_ammo")) +getBulletCount(handItemStake)+ "/" + DesertEagleItem.MAX_AMMO;

						if(anotherHandItemStake.getItem() instanceof DesertEagleItem){
							content = I18n.get("tips.simpledeserteagle.off_hand_ammo")+ ( isMainHand?getBulletCount(anotherHandItemStake):getBulletCount(handItemStake)) + "/"+ DesertEagleItem.MAX_AMMO+
									"      "+I18n.get("tips.simpledeserteagle.main_hand_ammo")+( isMainHand?getBulletCount(handItemStake):getBulletCount(anotherHandItemStake) )+ "/"+ DesertEagleItem.MAX_AMMO;
						}

						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \"" +content+"\"");
					}

				} else if (hand == InteractionHand.MAIN_HAND && entity instanceof Player player && player.getOffhandItem().getItem() instanceof DesertEagleItem) {//如果副手有枪就使用副手试试
					((Player)entity).getOffhandItem().getItem().use((Level) world,(Player) entity,InteractionHand.OFF_HAND);
				} else {//都没有就需要换弹了

//					DesertEagleReloadProcedure.execute(world, entity);
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"title @p actionbar \""+I18n.get("tips.simpledeserteagle.reloadbutton", KeyMappingsTest.RELOAD.saveString().toUpperCase())+"\"");

				}
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
}
