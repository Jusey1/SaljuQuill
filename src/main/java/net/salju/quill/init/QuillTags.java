package net.salju.quill.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;

public class QuillTags {
	public static final TagKey<Block> DND = BlockTags.create(new ResourceLocation("quill:dnd"));
	public static final TagKey<Block> AXER = BlockTags.create(new ResourceLocation("quill:axe_blocks"));
	public static final TagKey<Block> LB = BlockTags.create(new ResourceLocation("minecraft:ladders"));
	public static final TagKey<Item> LI = ItemTags.create(new ResourceLocation("minecraft:ladders"));
	public static final TagKey<Item> SHIELDS = ItemTags.create(new ResourceLocation("quill:shields"));
}