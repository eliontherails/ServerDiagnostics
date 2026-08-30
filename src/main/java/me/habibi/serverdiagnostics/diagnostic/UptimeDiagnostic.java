package me.habibi.serverdiagnostics.diagnostic;

public class UptimeDiagnostic {

    private final long startTime;

    public UptimeDiagnostic(long startTime) {
        this.startTime = startTime;
    }

    public DiagnosticResult checkUptime() {

        long uptimeMillis =
                System.currentTimeMillis() - startTime;

        long uptimeSeconds = uptimeMillis / 1000;

        long days = uptimeSeconds / 86400;
        long hours = (uptimeSeconds % 86400) / 3600;
        long minutes = (uptimeSeconds % 3600) / 60;

        String uptime;

        if (days > 0) {
            uptime = String.format(
                    "%dd %02dh %02dm",
                    days,
                    hours,
                    minutes
            );
        } else {
            uptime = String.format(
                    "%02dh %02dm",
                    hours,
                    minutes
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.GOOD,
                "Server uptime: " + uptime,
                "No uptime issues detected."
        );
    }
}