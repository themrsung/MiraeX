package me.sjun.dev.mirae.vault;

import me.sjun.dev.mirae.MiraeX;
import me.sjun.dev.mirae.account.AccountLedger;
import me.sjun.dev.mirae.account.MXAccount;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

/**
 * Vault adapter.
 */
public final class VaultAdapter implements Economy {
    /**
     * Creates a new adapter.
     *
     * @param ledger The account ledger
     */
    public VaultAdapter(@NotNull AccountLedger ledger) {
        this.ledger = ledger;
    }

    /**
     * The ledger instance.
     */
    private final @NotNull AccountLedger ledger;

    @Override
    public boolean isEnabled() {
        return MiraeX.isLoaded() && MiraeX.getInstance().isEnabled();
    }

    @Override
    public String getName() {
        return "MiraeX for Vault.";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return NumberFormat.getNumberInstance().format(v)
                + MiraeX.getInstance().getMiraeConfig().getCurrencySymbol();
    }

    @Override
    public String currencyNamePlural() {
        return MiraeX.getInstance().getMiraeConfig().getCurrencyName();
    }

    @Override
    public String currencyNameSingular() {
        return MiraeX.getInstance().getMiraeConfig().getCurrencyName();
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return ledger.getAccount(offlinePlayer.getUniqueId()).isPresent();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return ledger.getAccount(offlinePlayer.getUniqueId()).map(MXAccount::getBalance).orElse(0d);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return ledger.getAccount(offlinePlayer.getUniqueId()).map(MXAccount::getBalance).orElse(0d) >= v;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return ledger.getAccount(offlinePlayer.getUniqueId()).map(account -> {
            if (account.getBalance() < v)
                return new EconomyResponse(v, account.getBalance(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
            ledger.subtractBalance(account, v, "Withdrawal by Vault.");
            return new EconomyResponse(v, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, "Success");
        }).orElse(new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Account not found"));
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return ledger.getAccount(offlinePlayer.getUniqueId()).map(account -> {
            ledger.addBalance(account, v, "Deposit by Vault.");
            return new EconomyResponse(v, account.getBalance(), EconomyResponse.ResponseType.SUCCESS, "Success");
        }).orElse(new EconomyResponse(v, 0, EconomyResponse.ResponseType.FAILURE, "Account not found"));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        try {
            ledger.createAccount(offlinePlayer);
            return true;
        } catch (IllegalArgumentException e) {
            MiraeX.getInstance().getLogger().warning("Vault tried to create account for already existing player " + offlinePlayer.getUniqueId());
            return false;
        }
    }

    @Override
    public boolean hasAccount(String s) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String s) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(String s, String s1) {
        return getBalance(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String s, double v) {
        return has(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return has(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(Bukkit.getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported.");
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return createPlayerAccount(offlinePlayer);
    }
}
