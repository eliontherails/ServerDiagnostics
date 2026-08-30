package me.habibi.serverdiagnostics.diagnostic;

import org.bukkit.Server;

public class PlayerDiagnostic {

    private final Server server;

    public PlayerDiagnostic(Server server) {
        this.server = server;
    }

    public DiagnosticResult checkPlayers() {

        int onlinePlayers = server.getOnlinePlayers().size();
        int maxPlayers = server.getMaxPlayers();

        if (onlinePlayers == 0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "No players are currently online",
                    "Player load is currently zero."
            );
        }

        double percentage =
                (double) onlinePlayers / maxPlayers * 100.0;

        if (percentage < 80.0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    String.format(
                            "Player load is normal (%d/%d)",
                            onlinePlayers,
                            maxPlayers
                    ),
                    "Player count is within server capacity."
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.WARNING,
                String.format(
                        "Player count is high (%d/%d)",
                        onlinePlayers,
                        maxPlayers
                ),
                "High player counts can increase server workload."
        );
    }
}