
package net.pinero.simpledeserteagle.entity;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModEntities;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class DesertEagleBulletEntity extends AbstractArrow implements ItemSupplier {
	public DesertEagleBulletEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(SimpledeserteagleModEntities.DESERT_EAGLE_BULLET.get(), world);
	}

	public DesertEagleBulletEntity(EntityType<? extends DesertEagleBulletEntity> type, Level world) {
		super(type, world);
	}

	public DesertEagleBulletEntity(EntityType<? extends DesertEagleBulletEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public DesertEagleBulletEntity(EntityType<? extends DesertEagleBulletEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return new ItemStack(SimpledeserteagleModItems.DESERT_EAGLE_BULLET.get());
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(SimpledeserteagleModItems.DESERT_EAGLE_AMMO.get());
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.tickCount>60){//不管在地上（保留弹孔）还是由于某种原因卡在天上（未知bug）统统灭了
			this.discard();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult p_36755_) {
		super.onHitBlock(p_36755_);
		if (level().getBlockState(p_36755_.getBlockPos()).getBlock() instanceof AbstractGlassBlock ||
				level().getBlockState(p_36755_.getBlockPos()).getBlock() instanceof StainedGlassPaneBlock ||
					level().getBlockState(p_36755_.getBlockPos()).getBlock() instanceof IronBarsBlock ironBarsBlock &&ironBarsBlock.getName().toString().contains("block.minecraft.glass_pane")) {
			level().destroyBlock(p_36755_.getBlockPos(), true);
			this.discard();

			if (this.getOwner() instanceof ServerPlayer _player && distanceTo(_player) >= 100) {
				Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("simpledeserteagle:shoot_hundred_meters"));
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					for (String criteria : _ap.getRemainingCriteria())
						_player.getAdvancements().award(_adv, criteria);
				}
			}

		}
	}

	@Override
	protected void onHitEntity(EntityHitResult p_36757_) {



		super.onHitEntity(p_36757_);
		Entity entity = p_36757_.getEntity();
		if (this.getOwner() instanceof ServerPlayer _player && entity.distanceTo(_player) >= 100) {
			Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("simpledeserteagle:shoot_hundred_meters"));
			AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
			if (!_ap.isDone()) {
				for (String criteria : _ap.getRemainingCriteria())
					_player.getAdvancements().award(_adv, criteria);
			}
		}

		//TODO:护甲穿透和爆头？
//		if(entity instanceof LivingEntity livingEntity){
//			livingEntity.getArmorValue();
//		}

//		p_36757_.getEntity().getBoundingBox();

	}

	public static DesertEagleBulletEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		DesertEagleBulletEntity entityarrow = new DesertEagleBulletEntity(SimpledeserteagleModEntities.DESERT_EAGLE_BULLET.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		return entityarrow;
	}

	public static DesertEagleBulletEntity shoot(LivingEntity entity, LivingEntity target) {
		DesertEagleBulletEntity entityarrow = new DesertEagleBulletEntity(SimpledeserteagleModEntities.DESERT_EAGLE_BULLET.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 5f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(0);
		entityarrow.setKnockback(1);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		return entityarrow;
	}
}
