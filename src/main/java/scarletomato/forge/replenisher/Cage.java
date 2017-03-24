package scarletomato.forge.replenisher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Cage {
	
	private World world;
	private BlockPos pos;
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	private static final IBlockState BARRIER = Blocks.BARRIER.getDefaultState();
	private static final IBlockState STONE = Blocks.STONE.getDefaultState();

	public Cage(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}
	
	public Cage create() {
		world.setBlockState(pos.down(), BEDROCK);
//		world.setBlockState(pos.up().up(), BARRIER);
		world.setBlockState(pos.up().north(), BARRIER);
		world.setBlockState(pos.up().south(), BARRIER);
		world.setBlockState(pos.up().east(), BARRIER);
		world.setBlockState(pos.up().west(), BARRIER);
		world.setBlockState(pos.north(), BARRIER);
		world.setBlockState(pos.south(), BARRIER);
		world.setBlockState(pos.east(), BARRIER);
		world.setBlockState(pos.west(), BARRIER);
		return this;
	}
	
	public Cage destroy() {
		world.setBlockState(pos.down(), STONE);
//		world.setBlockState(pos.up().up(), AIR);
		world.setBlockState(pos.up().north(), AIR);
		world.setBlockState(pos.up().south(), AIR);
		world.setBlockState(pos.up().east(), AIR);
		world.setBlockState(pos.up().west(), AIR);
		world.setBlockState(pos.north(), AIR);
		world.setBlockState(pos.south(), AIR);
		world.setBlockState(pos.east(), AIR);
		world.setBlockState(pos.west(), AIR);
		return this;
	}
}
