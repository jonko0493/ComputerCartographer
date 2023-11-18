package net.jonko0493.computercartographer;

import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlock;
import net.jonko0493.computercartographer.block.ComputerizedCartographerBlockEntity;
import net.jonko0493.computercartographer.integration.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

public class ComputerCartographer
{
	public static final String MOD_ID = "computer_cartographer";
	public static List<IMapIntegration> integrations = new ArrayList<>();
	public static final Logger Log = LoggerFactory.getLogger(MOD_ID);
	public static void log(String message) {
		Log.info(message);
	}
	public static void logWarning(String message) {
		Log.warn(message);
	}
	public static void logException(Exception e) {
		Log.error(e.getMessage());
		Log.error(Arrays.toString(e.getStackTrace()));
	}

	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM);
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK);
	public static Map<RegistrySupplier<? extends Block>, Item.Settings> BLOCK_ITEMS = new HashMap<>();
	public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);
	public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create("computercraft", RegistryKeys.ITEM_GROUP);

	public static RegistrySupplier<ComputerizedCartographerBlock> COMPUTERIZED_CARTOGRAPHER_BLOCK = registerBlockItem("computerized_cartographer", () -> new ComputerizedCartographerBlock(AbstractBlock.Settings.create().hardness(1.3f)));
	public static RegistrySupplier<BlockEntityType<ComputerizedCartographerBlockEntity>> COMPUTERIZED_CARTOGRAPHER_BLOCK_ENTITY = BLOCK_ENTITIES.register("computerized_cartographer_block_entity", () -> BlockEntityType.Builder.create(ComputerizedCartographerBlockEntity::new, COMPUTERIZED_CARTOGRAPHER_BLOCK.get()).build(null));

	public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	public static <T extends Item> RegistrySupplier<T> registerItem(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}

	public static <T extends Block> RegistrySupplier<T> registerBlockItem(String name, Supplier<T> block) {
		return registerBlockItem(name, block, new Item.Settings());
	}

	public static <T extends Block> RegistrySupplier<T> registerBlockItem(String name, Supplier<T> block, Item.Settings props) {
		RegistrySupplier<T> blockRegistered = registerBlock(name, block);
		BLOCK_ITEMS.put(blockRegistered, props);
		return blockRegistered;
	}

	public static void init() {
		initIntegrations();
		BLOCKS.register();
		BLOCK_ITEMS.forEach((block, itemprops) -> {
			BlockItem blockItem = new BlockItem(block.get(), itemprops);
			ITEMS.register(block.getId(), () -> blockItem);
			CreativeTabRegistry.append(RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier("computercraft:tab")), blockItem);
		});
		ITEMS.register();
		BLOCK_ENTITIES.register();
	}

	public static void initIntegrations() {
		Log.atInfo().log("Attempting to add computer cartographer integrations...");
		if (Platform.isModLoaded("bluemap")) {
			BlueMapIntegration blueMapIntegration = new BlueMapIntegration();
			if (blueMapIntegration.init()) {
				log("Registered BlueMap integration!");
				integrations.add(blueMapIntegration);
			}
		}
		if (Platform.isModLoaded("dynmap")) {
			DynmapIntegration dynmapIntegration = new DynmapIntegration();
			if (dynmapIntegration.init()) {
				log("Registered Dynmap integration!");
				integrations.add(dynmapIntegration);
			}
		}
		if (Platform.isModLoaded("pl3xmap")) {
			Pl3xMapIntegration pl3xMapIntegration = new Pl3xMapIntegration();
			if (pl3xMapIntegration.init()) {
				log("Registered Pl3xMap integration!");
				integrations.add(pl3xMapIntegration);
			}
		}
		if (Platform.isModLoaded("squaremap")) {
			SquaremapIntegration squaremapIntegration = new SquaremapIntegration();
			if (squaremapIntegration.init()) {
				log("Registered Squaremap integration!");
				integrations.add(squaremapIntegration);
			}
		}
	}
}
