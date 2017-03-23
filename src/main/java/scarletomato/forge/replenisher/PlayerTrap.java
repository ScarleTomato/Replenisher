package scarletomato.forge.replenisher;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class PlayerTrap {
	HashMap<EntityPlayer, BlockPos> positions;

	public PlayerTrap(HashMap<EntityPlayer, BlockPos> positions) {
		this.positions = positions;
	}

	@SubscribeEvent
	public void playerRespawn(PlayerTickEvent event) {
		Replenisher.tp(event.player, positions.get(event.player));
	}
}
