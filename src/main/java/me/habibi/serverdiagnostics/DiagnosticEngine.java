package me.habibi.serverdiagnostics;

import me.habibi.serverdiagnostics.diagnostic.DiagnosticResult;
import me.habibi.serverdiagnostics.diagnostic.JavaDiagnostic;
import me.habibi.serverdiagnostics.diagnostic.MemoryDiagnostic;
import me.habibi.serverdiagnostics.diagnostic.MsptDiagnostic;
import me.habibi.serverdiagnostics.diagnostic.PerformanceDiagnostic;
import me.habibi.serverdiagnostics.diagnostic.PlayerDiagnostic;
import me.habibi.serverdiagnostics.diagnostic.UptimeDiagnostic;
import org.bukkit.Server;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticEngine {

    private final Server server;
    private final PerformanceMonitor performanceMonitor;
    private final long startTime;

    public DiagnosticEngine(
            Server server,
            PerformanceMonitor performanceMonitor,
            long startTime
    ) {
        this.server = server;
        this.performanceMonitor = performanceMonitor;
        this.startTime = startTime;
    }

    public List<DiagnosticResult> runDiagnostics() {

        List<DiagnosticResult> results = new ArrayList<>();

        // Performance
        PerformanceDiagnostic performanceDiagnostic =
                new PerformanceDiagnostic(server);

        results.add(
                performanceDiagnostic.checkTPS()
        );

        MsptDiagnostic msptDiagnostic =
                new MsptDiagnostic(performanceMonitor);

        results.add(
                msptDiagnostic.checkMspt()
        );

        // Memory
        MemoryDiagnostic memoryDiagnostic =
                new MemoryDiagnostic();

        results.add(
                memoryDiagnostic.checkMemory()
        );

        // Players
        PlayerDiagnostic playerDiagnostic =
                new PlayerDiagnostic(server);

        results.add(
                playerDiagnostic.checkPlayers()
        );

        // Uptime
        UptimeDiagnostic uptimeDiagnostic =
                new UptimeDiagnostic(startTime);

        results.add(
                uptimeDiagnostic.checkUptime()
        );

        // Java
        JavaDiagnostic javaDiagnostic =
                new JavaDiagnostic();

        results.add(
                javaDiagnostic.checkJava()
        );





        return results;
    }
}