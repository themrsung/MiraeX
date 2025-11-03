package me.sjun.dev.mirae.command.admin;

import me.sjun.dev.mirae.MiraeX;
import me.sjun.dev.mirae.command.MXCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Reload configuration command.
 */
public final class ReloadConfigCommand extends MXCommand {
    /**
     * Constructor.
     */
    public ReloadConfigCommand() {
        super("reloadconfig", "MiraeX config reload", "/reloadconfig", List.of("configreload"));
        setPermission("miraex.admin");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (!testPermissionSilent(sender) || !sender.isOp()) {
            return failureFeedback(sender, NO_PERMISSION);
        }

        MiraeX.getInstance().getMiraeConfig();

        return successFeedback(sender, "설정을 불러왔습니다.");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        return List.of();
    }
}
