package me.habibi.serverdiagnostics;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import me.habibi.serverdiagnostics.diagnostic.DiagnosticResult;

import java.util.List;

public class DiagnosticCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final DiagnosticEngine diagnosticEngine;

    public DiagnosticCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.diagnosticEngine = new DiagnosticEngine(
                plugin.getServer(),
                ((ServerDiagnostics) plugin).getPerformanceMonitor(),
                ((ServerDiagnostics) plugin).getStartTime()
        );
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("diagnose")) {
                runDiagnostics(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("plugininfo")) {
                showPluginInfo(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("config")) {
                showConfigInfo(sender);
                return true;
            }

            sender.sendMessage(
                    ChatColor.RED + "Unknown subcommand: "
                            + ChatColor.WHITE + args[0]
            );

            return true;
        }

        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;

        String javaVersion = System.getProperty("java.version");
        String serverVersion = plugin.getServer().getVersion();

        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();

        long uptimeMillis = System.currentTimeMillis() - ((ServerDiagnostics) plugin).getStartTime();

        long uptimeSeconds = uptimeMillis / 1000;

        long hours = uptimeSeconds / 3600;
        long minutes = (uptimeSeconds % 3600) / 60;
        long seconds = uptimeSeconds % 60;

        double[] tps = plugin.getServer().getTPS();
        double currentTps = Math.min(tps[0], 20.0);

        double mspt = ((ServerDiagnostics) plugin)
                .getPerformanceMonitor()
                .getCurrentMspt();

        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "╔══════════════════════════════╗");
        sender.sendMessage(ChatColor.AQUA + "║     " + ChatColor.WHITE + "Server Diagnostics" + ChatColor.AQUA + "     ║");
        sender.sendMessage(ChatColor.AQUA + "╠══════════════════════════════╣");
        sender.sendMessage(ChatColor.GRAY + " Server: " + ChatColor.WHITE + serverVersion);
        sender.sendMessage(ChatColor.GRAY + " Java:   " + ChatColor.WHITE + javaVersion);
        sender.sendMessage(ChatColor.GRAY + " Players:" + ChatColor.WHITE + " " + onlinePlayers);
        sender.sendMessage(ChatColor.GRAY + " TPS:     " + ChatColor.WHITE + String.format("%.2f", currentTps));
        sender.sendMessage(ChatColor.GRAY + " Memory: " + ChatColor.WHITE + usedMemory + " MB / " + maxMemory + " MB");
        sender.sendMessage(ChatColor.GRAY + " Uptime:  " + ChatColor.WHITE +
                String.format("%02dh %02dm %02ds", hours, minutes, seconds));
        sender.sendMessage(ChatColor.AQUA + "╚══════════════════════════════╝");
        sender.sendMessage("");

        return true;
    }
    private void runDiagnostics(CommandSender sender) {

        List<DiagnosticResult> results = diagnosticEngine.runDiagnostics();

        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "╔══════════════════════════════╗");
        sender.sendMessage(ChatColor.AQUA + "║     " + ChatColor.WHITE + "Server Diagnostics" + ChatColor.AQUA + "     ║");
        sender.sendMessage(ChatColor.AQUA + "╠══════════════════════════════╣");

        for (DiagnosticResult result : results) {

            ChatColor color;

            switch (result.getLevel()) {
                case GOOD -> color = ChatColor.GREEN;
                case WARNING -> color = ChatColor.YELLOW;
                case CRITICAL -> color = ChatColor.RED;
                default -> color = ChatColor.WHITE;
            }

            sender.sendMessage(
                    color + "● " + ChatColor.WHITE + result.getMessage()
            );

            sender.sendMessage(
                    ChatColor.GRAY + "  → " + result.getRecommendation()
            );
        }

        sender.sendMessage(ChatColor.AQUA + "╚══════════════════════════════╝");
        sender.sendMessage("");
    }
    private void showPluginInfo(CommandSender sender) {

        org.bukkit.plugin.Plugin[] plugins =
                plugin.getServer()
                        .getPluginManager()
                        .getPlugins();

        int enabled = 0;
        int disabled = 0;

        for (org.bukkit.plugin.Plugin plugin : plugins) {

            if (plugin.isEnabled()) {
                enabled++;
            } else {
                disabled++;
            }
        }

        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "╔══════════════════════════════╗");
        sender.sendMessage(
                ChatColor.AQUA + "║      "
                        + ChatColor.WHITE
                        + "Plugin Information"
                        + ChatColor.AQUA
                        + "       ║"
        );
        sender.sendMessage(ChatColor.AQUA + "╠══════════════════════════════╣");

        sender.sendMessage(
                ChatColor.GRAY + " Total:   "
                        + ChatColor.WHITE + plugins.length
        );

        sender.sendMessage(
                ChatColor.GREEN + " Enabled: "
                        + ChatColor.WHITE + enabled
        );

        sender.sendMessage(
                ChatColor.RED + " Disabled:"
                        + ChatColor.WHITE + " " + disabled
        );

        sender.sendMessage(ChatColor.AQUA + "╠══════════════════════════════╣");

        for (org.bukkit.plugin.Plugin plugin : plugins) {

            ChatColor statusColor =
                    plugin.isEnabled()
                            ? ChatColor.GREEN
                            : ChatColor.RED;

            String status =
                    plugin.isEnabled()
                            ? "ENABLED"
                            : "DISABLED";

            String name = plugin.getName();

            String version =
                    plugin.getDescription().getVersion();

            sender.sendMessage(
                    statusColor + "● "
                            + ChatColor.WHITE + name
            );

            sender.sendMessage(
                    ChatColor.GRAY + "  Version: "
                            + ChatColor.WHITE + version
            );

            sender.sendMessage(
                    ChatColor.GRAY + "  Status: "
                            + statusColor + status
            );
        }

        sender.sendMessage(ChatColor.AQUA + "╚══════════════════════════════╝");
        sender.sendMessage("");
    }
    private void showConfigInfo(CommandSender sender) {

        org.bukkit.Server server = plugin.getServer();

        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "╔══════════════════════════════╗");
        sender.sendMessage(
                ChatColor.AQUA + "║     "
                        + ChatColor.WHITE
                        + "Server Configuration"
                        + ChatColor.AQUA
                        + "     ║"
        );
        sender.sendMessage(ChatColor.AQUA + "╠══════════════════════════════╣");

        sender.sendMessage(
                ChatColor.GRAY + " Online Mode: "
                        + ChatColor.WHITE
                        + (server.getOnlineMode()
                        ? "ENABLED"
                        : "DISABLED")
        );

        sender.sendMessage(
                ChatColor.GRAY + " View Distance: "
                        + ChatColor.WHITE
                        + server.getViewDistance()
        );

        sender.sendMessage(
                ChatColor.GRAY + " Simulation Distance: "
                        + ChatColor.WHITE
                        + server.getSimulationDistance()
        );

        sender.sendMessage(
                ChatColor.GRAY + " Max Players: "
                        + ChatColor.WHITE
                        + server.getMaxPlayers()
        );

        if (!server.getWorlds().isEmpty()) {

            sender.sendMessage(
                    ChatColor.GRAY + " Difficulty: "
                            + ChatColor.WHITE
                            + server.getWorlds()
                            .get(0)
                            .getDifficulty()
                            .name()
            );

            sender.sendMessage(
                    ChatColor.GRAY + " PvP: "
                            + ChatColor.WHITE
                            + (server.getWorlds()
                            .get(0)
                            .getPVP()
                            ? "ENABLED"
                            : "DISABLED")
            );

            sender.sendMessage(
                    ChatColor.GRAY + " Hardcore: "
                            + ChatColor.WHITE
                            + (server.getWorlds()
                            .get(0)
                            .isHardcore()
                            ? "ENABLED"
                            : "DISABLED")
            );
        }

        sender.sendMessage(
                ChatColor.GRAY + " Whitelist: "
                        + ChatColor.WHITE
                        + (server.hasWhitelist()
                        ? "ENABLED"
                        : "DISABLED")
        );

        sender.sendMessage(
                ChatColor.GRAY + " Spawn Protection: "
                        + ChatColor.WHITE
                        + server.getSpawnRadius()
        );

        sender.sendMessage(
                ChatColor.GRAY + " End Dimension: "
                        + ChatColor.WHITE
                        + (server.getAllowEnd()
                        ? "ENABLED"
                        : "DISABLED")
        );

        sender.sendMessage(ChatColor.AQUA + "╚══════════════════════════════╝");
        sender.sendMessage("");
    }
}