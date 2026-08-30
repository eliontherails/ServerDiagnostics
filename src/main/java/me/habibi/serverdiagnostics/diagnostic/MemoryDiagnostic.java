package me.habibi.serverdiagnostics.diagnostic;

public class MemoryDiagnostic {

    public DiagnosticResult checkMemory() {

        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();

        double usagePercentage =
                (double) usedMemory / maxMemory * 100.0;

        if (usagePercentage < 75.0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    String.format(
                            "Memory usage is healthy (%.1f%%)",
                            usagePercentage
                    ),
                    "No action required."
            );
        }

        if (usagePercentage < 90.0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    String.format(
                            "Memory usage is elevated (%.1f%%)",
                            usagePercentage
                    ),
                    "Monitor memory usage and investigate memory-heavy plugins."
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.CRITICAL,
                String.format(
                        "Memory usage is very high (%.1f%%)",
                        usagePercentage
                ),
                "Investigate memory usage and possible memory-heavy plugins."
        );
    }
}