package me.sjun.dev.mirae.storage;

import com.google.gson.*;
import me.sjun.dev.mirae.gson.GsonSerializer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Item storage.
 */
public final class ItemStorage implements Serializable {
    private static final long DEFAULT_MAX_STORAGE = 2000;

    /**
     * Creates a new item storage. Defaults to max 2000 items.
     */
    public ItemStorage() {
        this.itemMap = new ConcurrentHashMap<>();
        this.maxCapacity = DEFAULT_MAX_STORAGE;
    }

    /**
     * Creates a new item storage.
     *
     * @param s The storage to copy
     */
    public ItemStorage(@NotNull ItemStorage s) {
        this.itemMap = Map.copyOf(s.itemMap); // No need to deep copy
        this.maxCapacity = s.maxCapacity;
    }

    private final @NotNull Map<ItemStack, Long> itemMap;
    private long maxCapacity;

    /**
     * Returns the modified stack.
     *
     * @param stack    The stack
     * @param modifier The modifier
     * @return The modified stack
     */
    private static @NotNull ItemStack modifiedStack(@NotNull ItemStack stack, @NotNull Consumer<@NotNull ItemStack> modifier) {
        ItemStack cloned = stack.clone();
        modifier.accept(cloned);
        return cloned;
    }

    /**
     * Returns the standardized stack.
     *
     * @param stack The stack
     * @return The standardized stack
     */
    private static @NotNull ItemStack standardizedStack(@NotNull ItemStack stack) {
        return modifiedStack(stack, s -> s.setAmount(1));
    }

    /**
     * Returns the max capacity.
     *
     * @return The max capacity
     */
    public long getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Returns the capacity used.
     *
     * @return The capacity used
     */
    public long getUsedCapacity() {
        return itemMap.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Returns the capacity remaining.
     *
     * @return The capacity remaining
     */
    public long getRemainingCapacity() {
        return maxCapacity - getUsedCapacity();
    }

    /**
     * Sets the max capacity.
     *
     * @param maxCapacity The max capacity
     */
    public void setMaxCapacity(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Returns the quantity.
     *
     * @param item The item
     * @return The quantity
     */
    public long getQuantity(@Nullable ItemStack item) {
        if (!Objects.nonNull(item)) return 0;
        return itemMap.getOrDefault(standardizedStack(item), 0L);
    }

    /**
     * Checks quantity.
     *
     * @param item     The item
     * @param quantity The quantity
     * @return {@code true} if this storage has the quantity
     */
    public boolean hasQuantity(@Nullable ItemStack item, long quantity) {
        return getQuantity(item) >= quantity;
    }

    /**
     * Adds items to this storage.
     *
     * @param item     The item to add
     * @param quantity The quantity to add
     * @return {@code true} If it was successfully added
     */
    public boolean addItems(@NotNull ItemStack item, long quantity) {
        if (quantity >= getRemainingCapacity()) {
            return false;
        }

        ItemStack std = standardizedStack(item);
        long quantityAfter = getQuantity(std) + quantity;
        itemMap.put(std, quantityAfter);
        return true;
    }

    /**
     * Forcefully adds items.
     *
     * @param item     The item to add
     * @param quantity The quantity to add
     */
    public void forceAddItems(@NotNull ItemStack item, long quantity) {
        ItemStack std = standardizedStack(item);
        long quantityAfter = getQuantity(std) + quantity;
        itemMap.put(std, quantityAfter);
    }

    /**
     * Removes items from this storage.
     *
     * @param item     The item to remove
     * @param quantity The quantity to remove
     * @return {@code true} if it was successfully removed
     */
    public boolean removeItems(@NotNull ItemStack item, long quantity) {
        ItemStack std = standardizedStack(item);
        long quantityBefore = getQuantity(std);
        if (quantity > quantityBefore) {
            return false;
        }

        long quantityAfter = quantityBefore - quantity;
        itemMap.put(std, quantityAfter);
        return true;
    }

    /**
     * Forcefully removes items.
     *
     * @param item     The item to remove
     * @param quantity The quantity to remove
     */
    public void forceRemoveItems(@NotNull ItemStack item, long quantity) {
        ItemStack std = standardizedStack(item);
        long quantityAfter = getQuantity(std) - quantity;
        itemMap.put(std, quantityAfter);
    }

    /**
     * Forcefully bulk adds items.
     *
     * @param storage The storage to copy from
     */
    public void addItems(@NotNull ItemStorage storage) {
        storage.itemMap.forEach(this::forceAddItems);
    }

    /**
     * Clears the items in this storage.
     */
    public void clearItems() {
        itemMap.clear();
    }

    /**
     * Returns the JSON serializer.
     *
     * @return The JSON serializer
     */
    public static @NotNull Serializer serializer() {
        return Serializer.serializer;
    }

    /**
     * JSON serialization.
     */
    public static final class Serializer implements GsonSerializer<ItemStorage> {
        private static final Serializer serializer = new Serializer();

        private Serializer() {
        }

        @Override
        public JsonElement serialize(ItemStorage itemStorage, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("itemMap", GsonSerializer.serializeMap(itemStorage.itemMap, context));
            object.addProperty("maxCapacity", itemStorage.maxCapacity);
            return object;
        }

        @Override
        public ItemStorage deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = element.getAsJsonObject();

            Map<ItemStack, Long> map = GsonSerializer.deserializeMap(object.get("itemMap"), context, ItemStack.class, Long.class);
            ItemStorage storage = new ItemStorage();
            storage.itemMap.putAll(map);

            if (object.has("maxCapacity")) {
                storage.maxCapacity = object.get("maxCapacity").getAsLong();
            }

            return storage;
        }
    }
}
