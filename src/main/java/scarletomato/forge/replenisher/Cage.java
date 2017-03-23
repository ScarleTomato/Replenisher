package scarletomato.forge.replenisher;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Cage {
	
	private World world;
	private BlockPos pos;

	public Cage(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}
	
	public Cage create() {
		world.setBlockState(pos.down(), Blocks.BEDROCK.getDefaultState());
		world.setBlockState(pos.up().north(), Blocks.BARRIER.getDefaultState());
		world.setBlockState(pos.up().south(), Blocks.BARRIER.getDefaultState());
		world.setBlockState(pos.up().east(), Blocks.BARRIER.getDefaultState());
		world.setBlockState(pos.up().west(), Blocks.BARRIER.getDefaultState());
		return this;
	}
	
	public Cage destroy() {
		world.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
		world.setBlockState(pos.up().north(), Blocks.AIR.getDefaultState());
		world.setBlockState(pos.up().south(), Blocks.AIR.getDefaultState());
		world.setBlockState(pos.up().east(), Blocks.AIR.getDefaultState());
		world.setBlockState(pos.up().west(), Blocks.AIR.getDefaultState());
		return this;
	}
}
