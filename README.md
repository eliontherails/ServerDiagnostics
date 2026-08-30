

Minecraft paper plugin for checking server performance and information

Checks things like TPS memory MSPT players uptime java plugins and server settings


Main files

  ServerDiagnostics.java - main plugin class

  DiagnosticCommand.java - handles the /serverdiag command

  DiagnosticEngine.java - runs the diagnostics

  PerformanceMonitor.java - monitors server performance


Commands

  /serverdiag - shows server information

  /serverdiag diagnose - checks server health

  /serverdiag plugininfo - shows loaded plugins and their status

  /serverdiag config - shows server configuration


Requires Paper 1.21.1 and Java 21

Build with mvn clean package 
