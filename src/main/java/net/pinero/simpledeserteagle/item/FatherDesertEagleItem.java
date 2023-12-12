
package net.pinero.simpledeserteagle.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import net.pinero.simpledeserteagle.init.SimpledeserteagleModItems;
import net.pinero.simpledeserteagle.item.renderer.FatherDesertEagleItemRenderer;
import net.pinero.simpledeserteagle.procedures.DesertEagleRightClickAirProcedure;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoItem;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.List;
import java.util.function.Consumer;
/**
 * 为了子沙鹰类做准备，省的写很多重复的代码
 */

public class FatherDesertEagleItem extends Item implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public String animationprocedure = "empty";

	public static final String RELOADING_DONE_TAG = "isReloading";
	public static ItemDisplayContext transformType;

	public static final int RELOAD_TIME = 2000;

	protected float fireDamage = 0;//伤害值

	protected int coolDownTick = 8;

	protected float power = 15;//初速度

	public final static int MAX_AMMO = 7;

	protected RegistryObject<Item> ammoType;

	public FatherDesertEagleItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC).setNoRepair().defaultDurability(MAX_AMMO));
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
		ammoType = SimpledeserteagleModItems.DESERT_EAGLE_AMMO;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		super.initializeClient(consumer);
		consumer.accept(new IClientItemExtensions() {
			private final BlockEntityWithoutLevelRenderer renderer = new FatherDesertEagleItemRenderer();

			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		});
	}

	public void getTransformType(ItemDisplayContext type) {
		this.transformType = type;
	}

	private PlayState idlePredicate(AnimationState event) {
		if (this.transformType != null ? true : false) {
			if (this.animationprocedure.equals("empty")) {
				event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.DesertEagle.idle"));
				return PlayState.CONTINUE;
			}
		}
		return PlayState.STOP;
	}

	private PlayState procedurePredicate(AnimationState event) {
		if (this.transformType != null ? true : false) {
			if (!(this.animationprocedure.equals("empty")) && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
				event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
				if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
					this.animationprocedure = "empty";
					event.getController().forceAnimationReset();
				}
			}
		}
		return PlayState.CONTINUE;
	}

	public void fireAnim(Level level, Player player, ItemStack stack){
		if (level instanceof ServerLevel serverLevel){
			triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "Fire", "fire");
		}

	}

	public void reloadAnim(Level level, Player player, ItemStack stack){
		if (level instanceof ServerLevel serverLevel)
			triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "Reload", "reload");
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		AnimationController procedureController = new AnimationController(this, "procedureController", 0, this::procedurePredicate);
		data.add(procedureController);
		AnimationController idleController = new AnimationController(this, "idleController", 0, this::idlePredicate);
		data.add(idleController);
		data.add(new AnimationController<>(this, "Fire", 0, state -> PlayState.STOP)
				.triggerableAnim("fire", RawAnimation.begin().thenPlay("animation.DesertEagle.fire")));
		data.add(new AnimationController<>(this, "Reload", 0, state -> PlayState.STOP)
				.triggerableAnim("reload", RawAnimation.begin().thenPlay("animation.DesertEagle.reload")));

	}

	@Override
	public void verifyTagAfterLoad(CompoundTag tag) {
		tag.putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,true);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 1;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		DesertEagleRightClickAirProcedure.execute(world, entity, hand);
		return InteractionResultHolder.pass(entity.getItemInHand(hand));

	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {

//		if (selected && entity instanceof ServerPlayer player){
//			if(player.getMainHandItem().getItem() == this){
//			TODO:实现持枪动作，但是目前还不会
//			}
//		}

	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		list.add(Component.literal("Ammo: "+(MAX_AMMO - itemstack.getDamageValue())+"/"+MAX_AMMO));
		list.add(Component.literal("Damage: "+ fireDamage *16));
		list.add(Component.literal("Cooldown: "+coolDownTick*0.05+"s"));
		list.add(Component.literal("Ammo type: "+((ammoType==SimpledeserteagleModItems.DESERT_EAGLE_AMMO)?"Common":"Advanced")));
	}


	public float getFireDamage(){
		return fireDamage;
	}

	public int getCoolDownTick(){
		return coolDownTick;
	}

	public float getPower(){
		return power;
	}

	public RegistryObject<Item> getAmmoType(){
		return ammoType;
	}

}
