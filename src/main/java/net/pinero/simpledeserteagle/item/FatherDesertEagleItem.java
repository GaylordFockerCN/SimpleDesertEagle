
package net.pinero.simpledeserteagle.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

	//public static final String RELOADING_DONE_TAG = "isReloading";
	public boolean isReloading = false;
	public static ItemDisplayContext transformType;

	public static final int RELOAD_TIME = 2000;

	protected float fireDamage = 0;//伤害值

	protected int coolDownTick = 8;

	protected float power = 15;//初速度

	public static final int numAmmoItemsInGun = 1;

	public final static int MAX_AMMO = 7;

	protected RegistryObject<Item> ammoType;

	public FatherDesertEagleItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC).setNoRepair().defaultDurability(MAX_AMMO));//引入弹匣了再把这个删了
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
	public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
		super.onCraftedBy(stack, p_41448_, p_41449_);
		ItemStack bullet = stack.copy();
		bullet.setCount(1);
		bullet.setDamageValue(bullet.getMaxDamage());
		setBulletItemStack(stack,bullet,0);//初始弹药应该为零。。
		//stack.setDamageValue(MAX_AMMO);
	}

//	@Override
//	public void verifyTagAfterLoad(CompoundTag tag) {
//		tag.putBoolean(FatherDesertEagleItem.RELOADING_DONE_TAG,true);
//	}

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
		//MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
		//if(!mouseHandler.isRightPressed()){
			DesertEagleRightClickAirProcedure.execute(world, entity, hand);
		//}

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
		ItemStack bulletItemStack = getBulletItemStack(itemstack,0);
		int ammo = bulletItemStack.getMaxDamage()-bulletItemStack.getDamageValue();
		list.add(Component.literal("Ammo: "+ammo+"/"+MAX_AMMO));
		list.add(Component.literal("Damage: "+ fireDamage *16));
		list.add(Component.literal(String.format("Cooldown: %.2fs", coolDownTick*0.05)));
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

	public static ItemStack getBulletItemStack(ItemStack gun, int id) {
		// 如果枪械没有 NBT 标签，给它添加一个
		if (!gun.hasTag()) {
			gun.setTag(new CompoundTag());
			return ItemStack.EMPTY;
		}
		// 如果枪械的 NBT 标签中没有 "ammo" 标签，给它添加一个
		if (!gun.getTag().contains("ammo")) {
			ListTag ammoTagsList = new ListTag();
			for (int i = 0; i < numAmmoItemsInGun; i++) {
				ammoTagsList.add(new CompoundTag());
			}
			gun.getTag().put("ammo", ammoTagsList);
			return ItemStack.EMPTY;
		}
		// 获取子弹的 NBT 标签列表
		ListTag ammoTagsList = gun.getTag().getList("ammo", Tag.TAG_COMPOUND);
		// 获取特定位置的子弹的 NBT 标签
		CompoundTag ammoTags = ammoTagsList.getCompound(id);
		return ItemStack.of(ammoTags);
	}

	public static void setBulletItemStack(ItemStack gun, ItemStack bullet, int id) {
		// 如果枪械没有 NBT 标签，给它添加一个
		if (!gun.hasTag()) {
			gun.setTag(new CompoundTag());
		}
		// 如果枪械的 NBT 标签中没有 "ammo" 标签，给它添加一个
		if (!gun.getTag().contains("ammo")) {
			ListTag ammoTagsList = new ListTag();
			for (int i = 0; i < numAmmoItemsInGun; i++) {
				ammoTagsList.add(new CompoundTag());
			}
			gun.getTag().put("ammo", ammoTagsList);
		}
		// 获取子弹的 NBT 标签列表
		ListTag ammoTagsList = gun.getTag().getList("ammo", Tag.TAG_COMPOUND);
		// 获取特定位置的子弹的 NBT 标签
		CompoundTag ammoTags = ammoTagsList.getCompound(id);
		// 如果子弹为空，将对应位置的 NBT 标签设为 null
		if (bullet.isEmpty()) {
			ammoTags = new CompoundTag();
		} else {
			// 将子弹的 NBT 标签应用到特定位置
			bullet.save(ammoTags);
		}

	}



}
