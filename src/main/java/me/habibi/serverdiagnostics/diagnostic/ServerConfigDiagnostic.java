package me.habibi.serverdiagnostics.diagnostic;

import org.bukkit.Difficulty;
import org.bukkit.Server;

import java.util.ArrayList;
import java.util.List;

public class ServerConfigDiagnostic {

    private final Server server;

    public ServerConfigDiagnostic(Server server) {
        this.server = server;
    }

    public List<DiagnosticResult> checkConfiguration() {

        List<DiagnosticResult> results = new ArrayList<>();

        checkOnlineMode(results);
        checkViewDistance(results);
        checkSimulationDistance(results);
        checkMaxPlayers(results);
        checkDifficulty(results);
        checkPvP(results);
        checkSpawnProtection(results);
        checkWhitelist(results);
        checkEndDimension(results);
        checkHardcore(results);

        return results;
    }

    private void checkOnlineMode(List<DiagnosticResult> results) {

        if (server.getOnlineMode()) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Online mode is enabled",
                    "Authentication is handled by the server."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Online mode is disabled",
                    "Verify that offline mode is intentional."
            ));
        }
    }

    private void checkViewDistance(List<DiagnosticResult> results) {

        int viewDistance = server.getViewDistance();

        if (viewDistance <= 10) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "View distance is reasonable (" + viewDistance + ")",
                    "No action required."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "View distance is high (" + viewDistance + ")",
                    "Higher view distances can increase chunk and memory workload."
            ));
        }
    }

    private void checkSimulationDistance(List<DiagnosticResult> results) {

        int simulationDistance = server.getSimulationDistance();

        if (simulationDistance <= 10) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Simulation distance is reasonable (" + simulationDistance + ")",
                    "No action required."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Simulation distance is high (" + simulationDistance + ")",
                    "Higher simulation distances can increase server workload."
            ));
        }
    }

    private void checkMaxPlayers(List<DiagnosticResult> results) {

        int maxPlayers = server.getMaxPlayers();

        if (maxPlayers <= 100) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Maximum player count is " + maxPlayers,
                    "No action required."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Maximum player count is high (" + maxPlayers + ")",
                    "A high player limit can increase potential server workload."
            ));
        }
    }

    private void checkDifficulty(List<DiagnosticResult> results) {

        if (server.getWorlds().isEmpty()) {
            return;
        }

        Difficulty difficulty = server.getWorlds().get(0).getDifficulty();

        results.add(new DiagnosticResult(
                DiagnosticResult.Level.GOOD,
                "Server difficulty is " +
                        difficulty.name().toLowerCase(),
                "Difficulty is a gameplay setting."
        ));
    }

    private void checkPvP(List<DiagnosticResult> results) {

        boolean pvpEnabled = server.getWorlds().stream()
                .anyMatch(world -> world.getPVP());

        if (pvpEnabled) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "PvP is enabled",
                    "Player-versus-player combat is allowed."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "PvP is disabled",
                    "Player-versus-player combat is disabled."
            ));
        }
    }

    private void checkSpawnProtection(List<DiagnosticResult> results) {

        int spawnProtection = server.getSpawnRadius();

        if (spawnProtection == 0) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Spawn protection is disabled",
                    "No protected spawn radius is configured."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Spawn protection radius is " + spawnProtection,
                    "Spawn protection is enabled."
            ));
        }
    }

    private void checkWhitelist(List<DiagnosticResult> results) {

        if (server.hasWhitelist()) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Whitelist is enabled",
                    "Only permitted players can normally join."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Whitelist is disabled",
                    "The server is not using a whitelist."
            ));
        }
    }

    private void checkEndDimension(List<DiagnosticResult> results) {

        if (server.getAllowEnd()) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "The End dimension is enabled",
                    "Players can access the End."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "The End dimension is disabled",
                    "Players cannot normally access the End."
            ));
        }
    }

    private void checkHardcore(List<DiagnosticResult> results) {

        boolean hardcore = server.getWorlds().stream()
                .anyMatch(world -> world.isHardcore());

        if (hardcore) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Hardcore mode is enabled",
                    "At least one loaded world is hardcore."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Hardcore mode is disabled",
                    "Loaded worlds are not hardcore."
            ));
        }
    }
}