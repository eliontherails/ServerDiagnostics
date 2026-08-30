package me.habibi.serverdiagnostics.diagnostic;

public class JavaDiagnostic {

    public DiagnosticResult checkJava() {

        String version = System.getProperty("java.version");

        int majorVersion = getMajorVersion(version);

        if (majorVersion >= 21) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.GOOD,
                    "Java version is supported (" + version + ")",
                    "No action required."
            );
        }

        if (majorVersion >= 17) {

            return new DiagnosticResult(
                    DiagnosticResult.Level.WARNING,
                    "Java version is older (" + version + ")",
                    "Consider using a newer supported Java version."
            );
        }

        return new DiagnosticResult(
                DiagnosticResult.Level.CRITICAL,
                "Java version is very old (" + version + ")",
                "Upgrade Java to a supported version for modern Minecraft servers."
        );
    }

    private int getMajorVersion(String version) {

        try {

            if (version.startsWith("1.")) {
                return Integer.parseInt(
                        version.substring(2, 3)
                );
            }

            int dotIndex = version.indexOf('.');

            if (dotIndex == -1) {
                return Integer.parseInt(version);
            }

            return Integer.parseInt(
                    version.substring(0, dotIndex)
            );

        } catch (NumberFormatException exception) {
            return 0;
        }
    }
}