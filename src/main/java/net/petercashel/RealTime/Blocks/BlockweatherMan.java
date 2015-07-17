package net.petercashel.RealTime.Blocks;

import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.petercashel.RealTime.TileEntities.TileEntityweatherMan;

public class BlockweatherMan extends BlockContainer {

	public BlockweatherMan() {
		super(Material.iron);
		
		setCreativeTab(CreativeTabs.tabRedstone);
		setHardness(-1F);
		setBlockName("weatherMan");
		setBlockTextureName("RealTime:"+getUnlocalizedName());
		setStepSound(Block.soundTypeMetal);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileEntityweatherMan();
	}
}
