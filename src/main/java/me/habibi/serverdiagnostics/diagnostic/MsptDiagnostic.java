package me.habibi.serverdiagnostics.diagnostic;

import me.habibi.serverdiagnostics.PerformanceMonitor;

public class MsptDiagnostic {

    private final PerformanceMonitor performanceMonitor;

    public MsptDiagnostic(PerformanceMonitor performanceMonitor) {
        this.performanceMonitor = performanceMonitor;
    }

    public DiagnosticResult checkMspt() {

        double mspt = performanceMonitor.getCurrentMspt();

        if (mspt <= 25.0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    String.format("MSPT is healthy (%.2f ms)", mspt),
                    "Server tick processing time is normal."
            );
        }

        if (mspt <= 50.0) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    String.format("MSPT is elevated (%.2f ms)", mspt),
                    "Monitor server performance for heavy tasks."
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.CRITICAL,
                String.format("MSPT is high (%.2f ms)", mspt),
                "Investigate plugins, entities, chunks, or other heavy server tasks."
        );
    }
}