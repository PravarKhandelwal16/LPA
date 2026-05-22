package com.example.lpa.domain.models

/**
 * Domain model representing a single audited LPA operation log entry.
 *
 * @param id        Unique identifier for this log entry (UUID or DB row ID).
 * @param timestamp ISO-8601 timestamp string when the event occurred.
 * @param operation Human-readable description of the LPA operation performed.
 * @param level     Severity level of the log entry.
 * @param detail    Optional additional detail or stack trace fragment.
 */
data class LpaLogEntry(
    val id: String,
    val timestamp: String,
    val operation: String,
    val level: LogLevel,
    val detail: String? = null
)

/**
 * Severity levels for LPA operation log entries.
 */
enum class LogLevel {
    /** General informational event (e.g. "Profile list refreshed"). */
    INFO,

    /** Something unexpected happened but the operation completed. */
    WARNING,

    /** The operation failed with an error. */
    ERROR,

    /** Low-level diagnostic detail (only in debug builds). */
    DEBUG
}
