package net.mcreator.enhancedenchanting.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.Tag;
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
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			for (Entity entityiterator : player.level().players())
				if (entityiterator != player && entityiterator instanceof ServerPlayer playeriterator)
					PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncMessage(playeriterator.getData(PLAYER_VARIABLES), playeriterator.getId()));
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
		}
	}

	@SubscribeEvent
	public static void onPlayerTickUpdateSyncPlayerVariables(PlayerTickEvent.Post event) {
		if (event.getEntity() instanceof ServerPlayer player && player.getData(PLAYER_VARIABLES)._syncDirty) {
			PacketDistributor.sendToPlayersInDimension((ServerLevel) player.level(), new PlayerVariablesSyncMessage(player.getData(PLAYER_VARIABLES), player.getId()));
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
			clone.rollers_active = original.rollers_active;
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
		public static final String DATA_NAME = "enhanced_enchanting_worldvars";
		boolean _syncDirty = false;
		public boolean DoLimit = false;
		public ArrayList<Object> forbidden = new ArrayList<>();

		public static WorldVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			WorldVariables data = new WorldVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			DoLimit = nbt.getBoolean("DoLimit");
			forbidden = NbtArrayLists.loadGlobalWorld(nbt.getList("forbidden", Tag.TAG_COMPOUND), lookupProvider);
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("DoLimit", DoLimit);
			nbt.put("forbidden", NbtArrayLists.saveGlobalWorld(forbidden, lookupProvider));
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			this._syncDirty = true;
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(WorldVariables::new, WorldVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "enhanced_enchanting_mapvars";
		boolean _syncDirty = false;
		public boolean legendary_limit_do = true;
		public boolean ModifyNameOfItem = false;
		public boolean disable_flashes = false;
		public double max_upgrader_level = 74.0;

		public static MapVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			MapVariables data = new MapVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			legendary_limit_do = nbt.getBoolean("legendary_limit_do");
			ModifyNameOfItem = nbt.getBoolean("ModifyNameOfItem");
			disable_flashes = nbt.getBoolean("disable_flashes");
			max_upgrader_level = nbt.getDouble("max_upgrader_level");
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("legendary_limit_do", legendary_limit_do);
			nbt.putBoolean("ModifyNameOfItem", ModifyNameOfItem);
			nbt.putBoolean("disable_flashes", disable_flashes);
			nbt.putDouble("max_upgrader_level", max_upgrader_level);
			return nbt;
		}

		public void markSyncDirty() {
			this.setDirty();
			_syncDirty = true;
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new SavedData.Factory<>(MapVariables::new, MapVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) -> {
			buffer.writeInt(message.dataType);
			if (message.data != null)
				buffer.writeNbt(message.data.save(new CompoundTag(), buffer.registryAccess()));
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
						MapVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
					else
						WorldVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
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
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putDouble("gravity_pulse_time", gravity_pulse_time);
			nbt.putBoolean("speed", speed);
			nbt.putBoolean("tall", tall);
			nbt.putDouble("snowball_angle", snowball_angle);
			nbt.putDouble("snowball_cooldown", snowball_cooldown);
			nbt.putBoolean("AlreadySlept", AlreadySlept);
			nbt.putString("chained_by", chained_by);
			nbt.putString("first_chained_entity", first_chained_entity);
			nbt.putDouble("chain_level", chain_level);
			nbt.putBoolean("stuck_by_chain", stuck_by_chain);
			nbt.putDouble("chain_cooldown", chain_cooldown);
			nbt.putBoolean("chained", chained);
			nbt.putBoolean("ricochet", ricochet);
			nbt.putDouble("ricochet_level", ricochet_level);
			nbt.putDouble("explosive_arrow_luck", explosive_arrow_luck);
			nbt.putDouble("water_arrows_level", water_arrows_level);
			nbt.putDouble("gravity_radius", gravity_radius);
			nbt.putDouble("velocity_level", velocity_level);
			nbt.putDouble("chain_reaction_number_of_arrows", chain_reaction_number_of_arrows);
			nbt.putDouble("fire_focus_power", fire_focus_power);
			nbt.putDouble("poison_focus_do", poison_focus_do);
			nbt.putDouble("fire_damages_upgrade", fire_damages_upgrade);
			nbt.putDouble("poisonous_damages_upgrade", poisonous_damages_upgrade);
			nbt.putDouble("cold_focus_power", cold_focus_power);
			nbt.putDouble("cold_damages_upgrade", cold_damages_upgrade);
			nbt.putDouble("head_shot_strenght", head_shot_strenght);
			nbt.putDouble("wind_arrow_level", wind_arrow_level);
			nbt.putBoolean("fire_trail", fire_trail);
			nbt.putBoolean("rollers_active", rollers_active);
			nbt.putDouble("enchantmentRemoverCooldown", enchantmentRemoverCooldown);
			nbt.putBoolean("gravity_reverse", gravity_reverse);
			nbt.putString("seeking", seeking);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			gravity_pulse_time = nbt.getDouble("gravity_pulse_time");
			speed = nbt.getBoolean("speed");
			tall = nbt.getBoolean("tall");
			snowball_angle = nbt.getDouble("snowball_angle");
			snowball_cooldown = nbt.getDouble("snowball_cooldown");
			AlreadySlept = nbt.getBoolean("AlreadySlept");
			chained_by = nbt.getString("chained_by");
			first_chained_entity = nbt.getString("first_chained_entity");
			chain_level = nbt.getDouble("chain_level");
			stuck_by_chain = nbt.getBoolean("stuck_by_chain");
			chain_cooldown = nbt.getDouble("chain_cooldown");
			chained = nbt.getBoolean("chained");
			ricochet = nbt.getBoolean("ricochet");
			ricochet_level = nbt.getDouble("ricochet_level");
			explosive_arrow_luck = nbt.getDouble("explosive_arrow_luck");
			water_arrows_level = nbt.getDouble("water_arrows_level");
			gravity_radius = nbt.getDouble("gravity_radius");
			velocity_level = nbt.getDouble("velocity_level");
			chain_reaction_number_of_arrows = nbt.getDouble("chain_reaction_number_of_arrows");
			fire_focus_power = nbt.getDouble("fire_focus_power");
			poison_focus_do = nbt.getDouble("poison_focus_do");
			fire_damages_upgrade = nbt.getDouble("fire_damages_upgrade");
			poisonous_damages_upgrade = nbt.getDouble("poisonous_damages_upgrade");
			cold_focus_power = nbt.getDouble("cold_focus_power");
			cold_damages_upgrade = nbt.getDouble("cold_damages_upgrade");
			head_shot_strenght = nbt.getDouble("head_shot_strenght");
			wind_arrow_level = nbt.getDouble("wind_arrow_level");
			fire_trail = nbt.getBoolean("fire_trail");
			rollers_active = nbt.getBoolean("rollers_active");
			enchantmentRemoverCooldown = nbt.getDouble("enchantmentRemoverCooldown");
			gravity_reverse = nbt.getBoolean("gravity_reverse");
			seeking = nbt.getString("seeking");
		}

		public void markSyncDirty() {
			_syncDirty = true;
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int player) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(EnhancedEnchantingMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> {
			buffer.writeInt(message.player());
			buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt());
			message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
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
					player.getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()));
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}