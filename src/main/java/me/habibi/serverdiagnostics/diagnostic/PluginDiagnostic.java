package me.habibi.serverdiagnostics.diagnostic;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginDiagnostic {

    private final Server server;

    public PluginDiagnostic(Server server) {
        this.server = server;
    }

    public List<DiagnosticResult> checkPlugins() {

        List<DiagnosticResult> results = new ArrayList<>();

        Plugin[] plugins = server.getPluginManager().getPlugins();

        int total = plugins.length;
        int enabled = 0;
        int disabled = 0;

        for (Plugin plugin : plugins) {

            if (plugin.isEnabled()) {
                enabled++;
            } else {
                disabled++;
            }
        }

        checkPluginCount(results, total);
        checkDisabledPlugins(results, disabled, plugins);
        checkPluginVersions(results, plugins);
        checkDuplicateNames(results, plugins);
        checkPluginLoadStatus(results, plugins);

        return results;
    }

    private void checkPluginCount(
            List<DiagnosticResult> results,
            int total
    ) {

        if (total <= 30) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Plugin count is reasonable (" + total + ")",
                    "No unusually large plugin count was detected."
            ));

        } else if (total <= 50) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Plugin count is high (" + total + ")",
                    "Review whether all installed plugins are necessary."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Plugin count is very high (" + total + ")",
                    "A large plugin collection can increase maintenance and compatibility risks."
            ));
        }
    }

    private void checkDisabledPlugins(
            List<DiagnosticResult> results,
            int disabled,
            Plugin[] plugins
    ) {

        if (disabled == 0) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "All plugins are enabled",
                    "No disabled plugins were detected."
            ));

            return;
        }

        StringBuilder names = new StringBuilder();

        for (Plugin plugin : plugins) {

            if (!plugin.isEnabled()) {

                if (names.length() > 0) {
                    names.append(", ");
                }

                names.append(plugin.getName());
            }
        }

        if (disabled <= 2) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    disabled + " plugin(s) are disabled",
                    "Check disabled plugins: " + names
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.CRITICAL,
                    disabled + " plugins are disabled",
                    "Investigate disabled plugins: " + names
            ));
        }
    }

    private void checkPluginVersions(
            List<DiagnosticResult> results,
            Plugin[] plugins
    ) {

        int missingVersions = 0;

        for (Plugin plugin : plugins) {

            String version = plugin.getDescription().getVersion();

            if (version == null ||
                    version.isBlank() ||
                    version.equalsIgnoreCase("unknown")) {

                missingVersions++;
            }
        }

        if (missingVersions == 0) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Plugin version information is available",
                    "All loaded plugins provide version information."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    missingVersions +
                            " plugin(s) have missing version information",
                    "Check the plugin metadata or plugin.yml files."
            ));
        }
    }

    private void checkDuplicateNames(
            List<DiagnosticResult> results,
            Plugin[] plugins
    ) {

        List<String> names = new ArrayList<>();
        List<String> duplicates = new ArrayList<>();

        for (Plugin plugin : plugins) {

            String name = plugin.getName();

            if (names.contains(name)) {

                if (!duplicates.contains(name)) {
                    duplicates.add(name);
                }

            } else {

                names.add(name);
            }
        }

        if (duplicates.isEmpty()) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "No duplicate plugin names detected",
                    "Plugin names appear unique."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Duplicate plugin names detected",
                    "Review these plugins: " +
                            String.join(", ", duplicates)
            ));
        }
    }

    private void checkPluginLoadStatus(
            List<DiagnosticResult> results,
            Plugin[] plugins
    ) {

        int enabled = 0;

        for (Plugin plugin : plugins) {

            if (plugin.isEnabled()) {
                enabled++;
            }
        }

        if (enabled == plugins.length) {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Plugin loading completed successfully",
                    enabled + " plugin(s) are currently active."
            ));

        } else {

            results.add(new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Not all plugins loaded successfully",
                    enabled + "/" + plugins.length +
                            " plugins are currently active."
            ));
        }
    }
}