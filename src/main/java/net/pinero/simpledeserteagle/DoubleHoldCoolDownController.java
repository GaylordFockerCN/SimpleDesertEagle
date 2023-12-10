/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.pinero.simpledeserteagle as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.pinero.simpledeserteagle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.pinero.simpledeserteagle.item.FatherDesertEagleItem;

/**
 * @author LZY or (Pinero)
 * 利用原版物品冷却的特性使沙鹰双持状态下可以左右开火（不要小看这段代码，没有这段代码无法实现交替开火而且会变成一起开火）
 * 原版右键空气时左右两边都会触发use，但是主手会更快点，所以只要判断沙鹰双持且两物品不同（物品共用冷却）时让没冷却的那个物品迅速冷却1刻（防止一只手use完立马被调用）
 * 有一点很关键，开始是没有判断canFire的，会导致左手可以开火但是由于右手先点击而导致左手武器被禁用
 * {@link net.pinero.simpledeserteagle.item.FatherDesertEagleItem#canFire()}
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
			Item offHand = player.getOffhandItem().getItem();
			Item mainHand = player.getMainHandItem().getItem();
			if(mainHand instanceof FatherDesertEagleItem mainHandItem&& offHand instanceof FatherDesertEagleItem offHandItem){
				if(event.getHand() == InteractionHand.MAIN_HAND){
					if(!player.getCooldowns().isOnCooldown(offHand)&&mainHandItem.canFire()){//必须加上判断能否开火，否则会导致左手可开火但是右手得先装弹
						player.getCooldowns().addCooldown(offHand,1);
					}
				}else{
					if((!player.getCooldowns().isOnCooldown(mainHand)) && offHandItem.canFire()){
						player.getCooldowns().addCooldown(mainHand,1);
					}
				}

			}

		}

		@SubscribeEvent
		public static void serverLoad(ServerStartingEvent event) {

		}

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void clientLoad(FMLClientSetupEvent event) {
		}
	}
}
