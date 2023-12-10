
package net.pinero.simpledeserteagle.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
	public static ItemDisplayContext transformType;

	public static final int RELOAD_TIME = 2000;

	protected int ammo = 0;

	public final String AmmoTagName = "DesertEagleAmmo";
	public static String AMMO_TAG_NAME = "DesertEagleAmmo";

	public Player player = null;//为了实现获取NBT的操作（快夸我天才，虽然高耦合）(被迫用Public，在进入游戏时设置，否则容易为null

	protected float damage = 0;//伤害值

	protected float power = 15;//初速度

	public final static int MAX_AMMO = 7;
	private boolean isReloading = false;

	protected
	RegistryObject<Item> ammoType;

	public FatherDesertEagleItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
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
				event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.DesertEagle.normal"));
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
			//stack.getOrCreateTag().putString("geckoAnim", "animation.DesertEagle.Fire");
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
				.triggerableAnim("fire", RawAnimation.begin().thenPlay("animation.DesertEagle.Fire")));
		data.add(new AnimationController<>(this, "Reload", 0, state -> PlayState.STOP)
				.triggerableAnim("reload", RawAnimation.begin().thenPlay("animation.DesertEagle.reload")));

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
		//System.out.println(world.isClientSide+hand.toString());
		this.player = entity;
		ItemStack stack = ItemStack.EMPTY;
		if(this == this.player.getMainHandItem().getItem()){
			stack = this.player.getMainHandItem();
		}else if(this == this.player.getOffhandItem().getItem()){
			stack = this.player.getOffhandItem();
		}
		ammo =stack.getOrCreateTag().getInt(AmmoTagName);//通过NBT获取
		DesertEagleRightClickAirProcedure.execute(world, entity, hand);
		ItemStack finalStack = stack;
		new Thread(()->{
			try {
				Thread.sleep(2);//延迟一下可以让单人模式下动画不会重叠，但是服务端无效不知道为何 TODO:让服务端变得不抽风
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			finalStack.getOrCreateTag().putInt(AmmoTagName,ammo);
		}).start();
		return InteractionResultHolder.pass(entity.getItemInHand(hand));//翻Item源码学的

	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {

		/**
		 * 时代的眼泪舍不得删
		 */
		//及时保存物品子弹数（不能马上存档完保存不然会有物品切换和开火动画重叠...又得利用冷却的特性了...（天才）
		if ( entity instanceof Player player &&(!selected || player.getCooldowns().isOnCooldown(itemstack.getItem())) ){/**时代的眼泪舍不得删*/
				//System.out.println(""+itemstack.getTag()+" "+itemstack.getDescriptionId()+" "+slot+(player instanceof ServerPlayer));

//			if(slot == 0){
//				if(player.getMainHandItem().getItem()==player.getOffhandItem().getItem()){
//					player.getMainHandItem().getOrCreateTag().putInt(FatherDesertEagleItem.AMMO_TAG_NAME,ammo);
//				}else if(itemstack.getItem()==(player.getMainHandItem().getItem())){
//					player.getMainHandItem().getOrCreateTag().putInt(FatherDesertEagleItem.AMMO_TAG_NAME,ammo);
//				}else{
//					player.getOffhandItem().getOrCreateTag().putInt(FatherDesertEagleItem.AMMO_TAG_NAME,ammo);
//				}
//			}else{
//				ItemStack teststack = player.getInventory().items.get(slot);
//				if (teststack.getItem() == this) {
//					teststack.getOrCreateTag().putInt(FatherDesertEagleItem.AMMO_TAG_NAME,ammo);
//				}
//			}
//
//			if(itemstack.getOrCreateTag().getLong("GeckoLibID")==player.getInventory().getItem(slot).getOrCreateTag().getLong("GeckoLibID"))
//				itemstack.getOrCreateTag().putInt(AmmoTagName,ammo);
		}

//		if (selected && entity instanceof ServerPlayer player){
//			if(player.getMainHandItem().getItem() == this){
//			TODO:实现持枪动作，但是目前还不会
//			}
//		}

	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		int count = itemstack.getOrCreateTag().getInt(AmmoTagName);
		if(player != null){
			list.add(Component.literal("Ammo: "+count+"/"+FatherDesertEagleItem.MAX_AMMO));
			list.add(Component.literal("Damage: "+damage*16));
			list.add(Component.literal("Ammo type: "+((ammoType==SimpledeserteagleModItems.DESERT_EAGLE_AMMO)?"Common":"Advanced")));
		}
	}


	public boolean isFull(){
		return ammo == MAX_AMMO;
	}

	public int need(){
		return MAX_AMMO - ammo;
	}

	public float getDamage(){
		return damage;
	}

	public float getPower(){
		return power;
	}

	public RegistryObject<Item> getAmmoType(){
		return ammoType;
	}

	public void reload(int addon) {
		if(isReloading)return;
		isReloading = true;

		ItemStack stack = ItemStack.EMPTY;
		if(this == this.player.getMainHandItem().getItem()){
			stack = this.player.getMainHandItem();
		}else if(this == this.player.getOffhandItem().getItem()){
			stack = this.player.getOffhandItem();
		}
		stack.getOrCreateTag().putInt(AmmoTagName, ammo+addon);
		ammo +=addon;
		new Thread(()->{
			try {
				Thread.sleep(FatherDesertEagleItem.RELOAD_TIME);
				isReloading = false;
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}).start();
	}

	public int getAmmo() {
		return ammo;
	}

	public boolean canFire() {
		return !isReloading && ammo >0;
	}

	public void useAmmo() {
		if(ammo >0) {
			ammo--;
		}
	}

	public boolean isReloading() {
		return isReloading;
	}
}
