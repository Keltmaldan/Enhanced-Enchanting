package net.mcreator.enhancedenchanting.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.ProblemReporter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import net.mcreator.enhancedenchanting.EnhancedEnchantingMod;

import java.util.function.Supplier;
import java.util.ArrayList;

@EventBusSubscriber
public class EnhancedEnchantingModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, EnhancedEnchantingMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());
	public static ArrayList<Object> legendary = new ArrayList<>();

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		EnhancedEnchantingMod.addNetworkMessage(SavedDataSyncMessage.TYPE, SavedDataSyncMessage.STREAM_CODEC, SavedDataSyncMessage::handleData);
		EnhancedEnchantingMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@SubscribeEvent
	public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayersInDimension(player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
			player.getData(PLAYER_VARIABLES)._syncDirty = false;
		}
	}

	@SubscribeEvent
	public static void clonePlayer(PlayerEvent.Clone event) {
		PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
		PlayerVariables clone = new PlayerVariables();
		clone.gravity_pulse_time = original.gravity_pulse_time;
		clone.speed = original.speed;
		clone.tall = original.tall;
		clone.snowball_cooldown = original.snowball_cooldown;
		clone.AlreadySlept = original.AlreadySlept;
		clone.chained_by = original.chained_by;
		clone.first_chained_entity = original.first_chained_entity;
		clone.chain_level = original.chain_level;
		clone.stuck_by_chain = original.stuck_by_chain;
		clone.chain_cooldown = original.chain_cooldown;
		clone.chained = original.chained;
		clone.fire_focus_power = original.fire_focus_power;
		clone.poison_focus_do = original.poison_focus_do;
		clone.cold_focus_power = original.cold_focus_power;
		clone.cold_damages_upgrade = original.cold_damages_upgrade;
		clone.rollers_active = original.rollers_active;
		clone.gravity_reverse = original.gravity_reverse;
		if (!event.isWasDeath()) {
			clone.snowball_angle = original.snowball_angle;
			clone.ricochet = original.ricochet;
			clone.ricochet_level = original.ricochet_level;
			clone.explosive_arrow_luck = original.explosive_arrow_luck;
			clone.water_arrows_level = original.water_arrows_level;
			clone.gravity_radius = original.gravity_radius;
			clone.velocity_level = original.velocity_level;
			clone.chain_reaction_number_of_arrows = original.chain_reaction_number_of_arrows;
			clone.fire_damages_upgrade = original.fire_damages_upgrade;
			clone.poisonous_damages_upgrade = original.poisonous_damages_upgrade;
			clone.head_shot_strenght = original.head_shot_strenght;
			clone.wind_arrow_level = original.wind_arrow_level;
			clone.fire_trail = original.fire_trail;
			clone.enchantmentRemoverCooldown = original.enchantmentRemoverCooldown;
			clone.seeking = original.seeking;
		}
		event.getEntity().setData(PLAYER_VARIABLES, clone);
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData mapdata = MapVariables.get(event.getEntity().level());
			SavedData worlddata = WorldVariables.get(event.getEntity().level());
			if (mapdata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(0, mapdata));
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			SavedData worlddata = WorldVariables.get(event.getEntity().level());
			if (worlddata != null)
				PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
		}
	}

	@SubscribeEvent
	public static void onWorldTick(LevelTickEvent.Post event) {
		if (event.getLevel() instanceof ServerLevel level) {
			WorldVariables worldVariables = WorldVariables.get(level);
			if (worldVariables._syncDirty) {
				PacketDistributor.sendToPlayersInDimension(level, new SavedDataSyncMessage(1, worldVariables));
				worldVariables._syncDirty = false;
			}
			MapVariables mapVariables = MapVariables.get(level);
			if (mapVariables._syncDirty) {
				PacketDistributor.sendToAllPlayers(new SavedDataSyncMessage(0, mapVariables));
				mapVariables._syncDirty = false;
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final SavedDataType<WorldVariables> TYPE = new SavedDataType<>(Identifier.parse("enhanced_enchanting:worldvars"), level -> new WorldVariables(), level -> CompoundTag.CODEC.xmap(tag -> {
			WorldVariables instance = new WorldVariables();
			instance.read(tag, level.registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), level.registryAccess())));
		boolean _syncDirty = false;
		public boolean DoLimit = false;
		public ArrayList<Object> forbidden = new ArrayList<>();

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			DoLimit = nbt.getBooleanOr("DoLimit", false);
			forbidden = NbtArrayLists.loadGlobalWorld(nbt.getListOrEmpty("forbidden"), lookupProvider);
		}

		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("DoLimit", DoLimit);
			nbt.put("forbidden", NbtArrayLists.saveGlobalWorld(forbidden));
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(WorldVariables.TYPE);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final SavedDataType<MapVariables> TYPE = new SavedDataType<>(Identifier.parse("enhanced_enchanting:mapvars"), level -> new MapVariables(), level -> CompoundTag.CODEC.xmap(tag -> {
			MapVariables instance = new MapVariables();
			instance.read(tag, level.registryAccess());
			return instance;
		}, instance -> instance.save(new CompoundTag(), level.registryAccess())));
		boolean _syncDirty = false;
		public boolean legendary_limit_do = true;
		public boolean ModifyNameOfItem = false;
		public boolean disable_flashes = false;
		public double max_upgrader_level = 74.0;

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			legendary_limit_do = nbt.getBooleanOr("legendary_limit_do", false);
			ModifyNameOfItem = nbt.getBooleanOr("ModifyNameOfItem", false);
			disable_flashes = nbt.getBooleanOr("disable_flashes", false);
			max_upgrader_level = nbt.getDoubleOr("max_upgrader_level", 0);
		}

		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("legendary_limit_do", legendary_limit_do);
			nbt.putBoolean("ModifyNameOfItem", ModifyNameOfItem);
			nbt.putBoolean("disable_flashes", disable_flashes);
			nbt.putDouble("max_upgrader_level", max_upgrader_level);
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAccessor) {
				return serverLevelAccessor.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(MapVariables.TYPE);
			} else {
				return clientSide;
			}
		}
	}

	public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) -> {
			buffer.writeInt(message.dataType);
			if (message.data instanceof MapVariables mapVariables)
				buffer.writeNbt(mapVariables.save(new CompoundTag(), buffer.registryAccess()));
			else if (message.data instanceof WorldVariables worldVariables)
				buffer.writeNbt(worldVariables.save(new CompoundTag(), buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			int dataType = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			SavedData data = null;
			if (nbt != null) {
				data = dataType == 0 ? new MapVariables() : new WorldVariables();
				if (data instanceof MapVariables mapVariables)
					mapVariables.read(nbt, buffer.registryAccess());
				else if (data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt, buffer.registryAccess());
			}
			return new SavedDataSyncMessage(dataType, data);
		});

		@Override
		public Type<SavedDataSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final SavedDataSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					if (message.dataType == 0)
						MapVariables.clientSide.read(((MapVariables) message.data).save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
					else
						WorldVariables.clientSide.read(((WorldVariables) message.data).save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public static class PlayerVariables implements ValueIOSerializable {
		boolean _syncDirty = false;
		public double gravity_pulse_time = 1.0;
		public boolean speed = false;
		public boolean tall = false;
		public double snowball_angle = 0;
		public double snowball_cooldown = 0;
		public boolean AlreadySlept = false;
		public String chained_by = "\"\"";
		public String first_chained_entity = "\"\"";
		public double chain_level = 0.0;
		public boolean stuck_by_chain = false;
		public double chain_cooldown = 0;
		public boolean chained = false;
		public boolean ricochet = false;
		public double ricochet_level = 0;
		public double explosive_arrow_luck = 0;
		public double water_arrows_level = 0;
		public double gravity_radius = 0;
		public double velocity_level = 0;
		public double chain_reaction_number_of_arrows = 0;
		public double fire_focus_power = 0;
		public double poison_focus_do = 0;
		public double fire_damages_upgrade = 0;
		public double poisonous_damages_upgrade = 0;
		public double cold_focus_power = 0;
		public double cold_damages_upgrade = 0;
		public double head_shot_strenght = 0;
		public double wind_arrow_level = 0;
		public boolean fire_trail = false;
		public boolean rollers_active = false;
		public double enchantmentRemoverCooldown = 0.0;
		public boolean gravity_reverse = false;
		public String seeking = "\u00E7^null^\u00E7";

		@Override
		public void serialize(ValueOutput output) {
			output.putDouble("gravity_pulse_time", gravity_pulse_time);
			output.putBoolean("speed", speed);
			output.putBoolean("tall", tall);
			output.putDouble("snowball_angle", snowball_angle);
			output.putDouble("snowball_cooldown", snowball_cooldown);
			output.putBoolean("AlreadySlept", AlreadySlept);
			output.putString("chained_by", chained_by);
			output.putString("first_chained_entity", first_chained_entity);
			output.putDouble("chain_level", chain_level);
			output.putBoolean("stuck_by_chain", stuck_by_chain);
			output.putDouble("chain_cooldown", chain_cooldown);
			output.putBoolean("chained", chained);
			output.putBoolean("ricochet", ricochet);
			output.putDouble("ricochet_level", ricochet_level);
			output.putDouble("explosive_arrow_luck", explosive_arrow_luck);
			output.putDouble("water_arrows_level", water_arrows_level);
			output.putDouble("gravity_radius", gravity_radius);
			output.putDouble("velocity_level", velocity_level);
			output.putDouble("chain_reaction_number_of_arrows", chain_reaction_number_of_arrows);
			output.putDouble("fire_focus_power", fire_focus_power);
			output.putDouble("poison_focus_do", poison_focus_do);
			output.putDouble("fire_damages_upgrade", fire_damages_upgrade);
			output.putDouble("poisonous_damages_upgrade", poisonous_damages_upgrade);
			output.putDouble("cold_focus_power", cold_focus_power);
			output.putDouble("cold_damages_upgrade", cold_damages_upgrade);
			output.putDouble("head_shot_strenght", head_shot_strenght);
			output.putDouble("wind_arrow_level", wind_arrow_level);
			output.putBoolean("fire_trail", fire_trail);
			output.putBoolean("rollers_active", rollers_active);
			output.putDouble("enchantmentRemoverCooldown", enchantmentRemoverCooldown);
			output.putBoolean("gravity_reverse", gravity_reverse);
			output.putString("seeking", seeking);
		}

		@Override
		public void deserialize(ValueInput input) {
			gravity_pulse_time = input.getDoubleOr("gravity_pulse_time", 0);
			speed = input.getBooleanOr("speed", false);
			tall = input.getBooleanOr("tall", false);
			snowball_angle = input.getDoubleOr("snowball_angle", 0);
			snowball_cooldown = input.getDoubleOr("snowball_cooldown", 0);
			AlreadySlept = input.getBooleanOr("AlreadySlept", false);
			chained_by = input.getStringOr("chained_by", "");
			first_chained_entity = input.getStringOr("first_chained_entity", "");
			chain_level = input.getDoubleOr("chain_level", 0);
			stuck_by_chain = input.getBooleanOr("stuck_by_chain", false);
			chain_cooldown = input.getDoubleOr("chain_cooldown", 0);
			chained = input.getBooleanOr("chained", false);
			ricochet = input.getBooleanOr("ricochet", false);
			ricochet_level = input.getDoubleOr("ricochet_level", 0);
			explosive_arrow_luck = input.getDoubleOr("explosive_arrow_luck", 0);
			water_arrows_level = input.getDoubleOr("water_arrows_level", 0);
			gravity_radius = input.getDoubleOr("gravity_radius", 0);
			velocity_level = input.getDoubleOr("velocity_level", 0);
			chain_reaction_number_of_arrows = input.getDoubleOr("chain_reaction_number_of_arrows", 0);
			fire_focus_power = input.getDoubleOr("fire_focus_power", 0);
			poison_focus_do = input.getDoubleOr("poison_focus_do", 0);
			fire_damages_upgrade = input.getDoubleOr("fire_damages_upgrade", 0);
			poisonous_damages_upgrade = input.getDoubleOr("poisonous_damages_upgrade", 0);
			cold_focus_power = input.getDoubleOr("cold_focus_power", 0);
			cold_damages_upgrade = input.getDoubleOr("cold_damages_upgrade", 0);
			head_shot_strenght = input.getDoubleOr("head_shot_strenght", 0);
			wind_arrow_level = input.getDoubleOr("wind_arrow_level", 0);
			fire_trail = input.getBooleanOr("fire_trail", false);
			rollers_active = input.getBooleanOr("rollers_active", false);
			enchantmentRemoverCooldown = input.getDoubleOr("enchantmentRemoverCooldown", 0);
			gravity_reverse = input.getBooleanOr("gravity_reverse", false);
			seeking = input.getStringOr("seeking", "");
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, buffer.registryAccess());
			message.data.serialize(output);
			buffer.writeInt(message.player());
			buffer.writeNbt(output.buildResult());
		}, (RegistryFriendlyByteBuf buffer) -> {
			PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt());
			message.data.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, buffer.registryAccess(), buffer.readNbt()));
			return message;
		});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					Entity player = context.player().level().getEntity(message.player);
					if (player == null)
						return;
					TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, context.player().registryAccess());
					message.data.serialize(output);
					player.getData(PLAYER_VARIABLES).deserialize(TagValueInput.create(ProblemReporter.DISCARDING, context.player().registryAccess(), output.buildResult()));
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}