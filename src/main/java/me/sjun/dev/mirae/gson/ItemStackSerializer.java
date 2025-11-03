package me.sjun.dev.mirae.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Item Stack Serializer.
 */
public final class ItemStackSerializer implements GsonSerializer<ItemStack> {
    /**
     * Returns the serializer instance.
     *
     * @return The serializer instance
     */
    public static @NotNull ItemStackSerializer serializer() {
        return serializer;
    }

    private static final ItemStackSerializer serializer = new ItemStackSerializer();

    private ItemStackSerializer() {
    }

    @Override
    public JsonElement serialize(ItemStack stack, Type type, JsonSerializationContext context) {
        return context.serialize(stack.serialize());
    }

    @Override
    public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return ItemStack.deserialize(context.deserialize(element, Map.class));
    }
}
