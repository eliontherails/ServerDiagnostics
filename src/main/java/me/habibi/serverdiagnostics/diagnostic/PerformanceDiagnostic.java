package me.habibi.serverdiagnostics.diagnostic;

import org.bukkit.Server;

public class PerformanceDiagnostic {

    private final Server server;

    public PerformanceDiagnostic(Server server) {
        this.server = server;
    }

    public DiagnosticResult checkTPS() {

        double[] tps = server.getTPS();
        double tpsValue = Math.min(tps[0], 20.0);

        if (tpsValue >= 19.0) {
            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    String.format("TPS is healthy (%.2f)", tpsValue),
                    "No action required."
            );
        }

        if (tpsValue >= 16.0) {
            return new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    String.format("TPS is below ideal (%.2f)", tpsValue),
                    "Monitor server activity and plugin performance."
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.CRITICAL,
                String.format("TPS is critically low (%.2f)", tpsValue),
                "Investigate heavy entities, chunks, plugins, or other server tasks."
        );
    }
}