package me.habibi.serverdiagnostics.diagnostic;

public class DiagnosticResult {

    public enum Level {
        GOOD,
        WARNING,
        CRITICAL
    }

    private final Level level;
    private final String message;
    private final String recommendation;

    public DiagnosticResult(Level level, String message, String recommendation) {
        this.level = level;
        this.message = message;
        this.recommendation = recommendation;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getRecommendation() {
        return recommendation;
    }
}