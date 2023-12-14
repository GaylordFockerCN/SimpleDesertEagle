
package net.pinero.simpledeserteagle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.pinero.simpledeserteagle.item.FatherDesertEagleItem;
import net.pinero.simpledeserteagle.item.HeavyDesertEagleItem;

/**
 * @author LZY_Pinero
 * 利用原版物品冷却的特性使沙鹰双持状态下可以左右开火（不要小看这段代码，没有这段代码无法实现交替开火而且会变成一起开火）
 * 原版右键空气时左右两边都会触发use，但是主手会更快点，所以只要判断沙鹰双持且两物品不同（物品共用冷却）时让没冷却的那个物品迅速冷却1刻（防止一只手use完立马被调用）
 * 有一点很关键，开始是没有判断canFire的，会导致左手可以开火但是由于右手先点击而导致左手武器被禁用
 *
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DoubleHoldCoolDownController {
	public DoubleHoldCoolDownController() {
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		new DoubleHoldCoolDownController();
	}

	@Mod.EventBusSubscriber
	private static class ForgeBusEvents {

		@SubscribeEvent
		public static void doubleHoldCoolDownController(PlayerInteractEvent event) {

			Player player = event.getEntity();
			ItemStack offHandStack = player.getOffhandItem();
			ItemStack mainHandStack = player.getMainHandItem();
			if (offHandStack.getItem().getClass() == mainHandStack.getItem().getClass())return;
			if(mainHandStack.getItem() instanceof FatherDesertEagleItem mainHandItem&& offHandStack.getItem() instanceof FatherDesertEagleItem offHandItem){
				if(event.getHand() == InteractionHand.MAIN_HAND){
					if(!player.getCooldowns().isOnCooldown(offHandItem)&& !mainHandItem.isReloading){
						player.getCooldowns().addCooldown(offHandItem,1);
					}
				}else{
					if((!player.getCooldowns().isOnCooldown(mainHandItem)) && !offHandItem.isReloading){
						player.getCooldowns().addCooldown(mainHandItem,1);
					}
				}

			}
		}

//		@SubscribeEvent
//		public static void onHoldHeavy(TickEvent.PlayerTickEvent event) {
//			Player player = event.player;
//			if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof HeavyDesertEagleItem ||player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof HeavyDesertEagleItem){
//				player.setSpeed(0.5f);
//			}else player.setSpeed(1);
//		}

	}
}
