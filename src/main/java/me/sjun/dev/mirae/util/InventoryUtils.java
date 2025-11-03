package me.sjun.dev.mirae.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Inventory utilities.
 */
public final class InventoryUtils {
    /**
     * Returns the item's quantity.
     *
     * @param inventory The inventory
     * @param item      The item
     * @return The item's quantity
     */
    public static int getQuantity(@NotNull Inventory inventory, @NotNull ItemStack item) {
        return Arrays.stream(inventory.getStorageContents())
                .filter(Objects::nonNull)
                .filter(item::isSimilar)
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    /**
     * Checks for quantity.
     *
     * @param inventory The inventory
     * @param item      The item
     * @param quantity  The quantity
     * @return {@code true} if sufficient
     */
    public static boolean hasQuantity(@NotNull Inventory inventory, @NotNull ItemStack item, int quantity) {
        return getQuantity(inventory, item) >= quantity;
    }

    /**
     * Returns the remaining space.
     *
     * @param inventory The inventory
     * @param item      The item
     * @return The remaining space
     */
    public static int getRemainingSpaceFor(@NotNull Inventory inventory, @NotNull ItemStack item) {
        int stackSize = item.getType().getMaxStackSize();
        int emptySlots = (int) Arrays.stream(inventory.getStorageContents())
                .filter(stack -> !Objects.nonNull(stack) || stack.getType() == Material.AIR)
                .count();

        return stackSize * emptySlots + Arrays.stream(inventory.getStorageContents())
                .filter(Objects::nonNull)
                .filter(item::isSimilar)
                .mapToInt(stack -> stackSize - stack.getAmount())
                .sum();
    }

    /**
     * Checks for remaining space.
     *
     * @param inventory The inventory
     * @param item      The item
     * @param quantity  The quantity
     * @return {@code true} if sufficient
     */
    public static boolean hasRemainingSpaceFor(@NotNull Inventory inventory, @NotNull ItemStack item, int quantity) {
        return getRemainingSpaceFor(inventory, item) >= quantity;
    }

    /**
     * Gives items to player.
     *
     * @param player   The player
     * @param item     The item
     * @param quantity The quantity
     * @return The remainder which could not be given
     */
    public static int giveItemsToPlayer(@NotNull Player player, @NotNull ItemStack item, int quantity) {
        return giveItemsToPlayer(player, item, quantity, false);
    }

    /**
     * Gives items to player.
     *
     * @param player                 The player
     * @param item                   The item
     * @param quantity               The quantity
     * @param mustDeliverToInventory Whether it must be delivered to inventory
     * @return The remainder which could not be given
     */
    public static int giveItemsToPlayer(@NotNull Player player, @NotNull ItemStack item, int quantity, boolean mustDeliverToInventory) {
        int remainder = giveItems(player.getInventory(), item, quantity);
        int stackSize = item.getType().getMaxStackSize();

        int stackCount = remainder / stackSize;
        int lastStack = remainder % stackSize;

        if (!mustDeliverToInventory) {
            List<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < stackCount; i++) {
                ItemStack full = item.clone();
                full.setAmount(stackSize);
                stacks.add(full);

                remainder -= stackSize;
            }

            if (lastStack > 0) {
                ItemStack partial = item.clone();
                partial.setAmount(lastStack);
                stacks.add(partial);

                remainder -= lastStack;
            }

            stacks.forEach(stack -> player.getWorld().dropItem(player.getLocation(), stack));
        }

        return remainder;
    }

    /**
     * Takes items from player.
     *
     * @param player   The player
     * @param item     The item
     * @param quantity The quantity
     * @return The remainder which could not be taken
     */
    public static int takeItemsFromPlayer(@NotNull Player player, @NotNull ItemStack item, int quantity) {
        return takeItems(player.getInventory(), item, quantity);
    }

    /**
     * Gives items to inventory.
     *
     * @param inventory The inventory to give to
     * @param item      The item
     * @param quantity  The quantity
     * @return The remainder which could not be given
     */
    public static int giveItems(@NotNull Inventory inventory, @NotNull ItemStack item, int quantity) {
        int stackSize = item.getType().getMaxStackSize();
        int remaining = Math.min(getRemainingSpaceFor(inventory, item), quantity);
        ItemStack[] contents = inventory.getStorageContents();

        for (int i = 0; i < contents.length; i++) {
            if (remaining <= 0) return quantity;

            ItemStack existing = contents[i];
            if (!Objects.nonNull(existing) || existing.getType() == Material.AIR) {
                ItemStack full = item.clone();
                int toGive = Math.min(remaining, stackSize);
                full.setAmount(toGive);
                contents[i] = full;
                remaining -= toGive;
            } else if (item.isSimilar(existing)) {
                int present = contents[i].getAmount();
                int toGive = Math.min(stackSize - present, remaining);
                contents[i].setAmount(present + toGive);
                remaining -= toGive;
            }
        }

        inventory.setStorageContents(contents);

        return remaining;
    }

    /**
     * Takes items from inventory.
     *
     * @param inventory The inventory to take from
     * @param item      The item
     * @param quantity  The quantity
     * @return The remainder which could not be taken
     */
    public static int takeItems(@NotNull Inventory inventory, @NotNull ItemStack item, int quantity) {
        int remaining = quantity;
        ItemStack[] contents = inventory.getStorageContents();

        for (int i = 0; i < contents.length; i++) {
            if (item.isSimilar(contents[i])) {
                int available = contents[i].getAmount();
                int toTake = Math.min(remaining, available);

                if (toTake >= contents[i].getAmount()) {
                    contents[i] = ItemStack.empty();
                } else {
                    contents[i].setAmount(available - toTake);
                }

                remaining -= toTake;
            }
        }

        inventory.setStorageContents(contents);
        return remaining;
    }

    /**
     * Prevents instantiation.
     *
     * @throws Exception Always
     */
    private InventoryUtils() throws Exception {
        throw new Exception("Cannot instantiate utility class.");
    }
}
