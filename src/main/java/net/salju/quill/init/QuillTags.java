package net.salju.quill.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class QuillTags {
	public static final TagKey KOBOLDS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("kobolds:kobolds"));
	public static final TagKey<Block> DND = BlockTags.create(new ResourceLocation("quill:dnd"));
	public static final TagKey<Block> AXER = BlockTags.create(new ResourceLocation("quill:axe_blocks"));
	public static final TagKey<Item> SHIELDS = ItemTags.create(new ResourceLocation("quill:shields"));
	public static final TagKey<Item> CANNON = ItemTags.create(new ResourceLocation("quill:cannon_recolor"));
	public static final TagKey<Item> DOUBENCHS = ItemTags.create(new ResourceLocation("quill:double_enchantments"));
}