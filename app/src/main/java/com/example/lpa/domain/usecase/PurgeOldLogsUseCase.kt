package com.example.lpa.domain.usecase

import com.example.lpa.core.result.Result
import com.example.lpa.domain.repository.LogRepository
import javax.inject.Inject

/**
 * Use case to purge log entries older than a specified retention period.
 */
class PurgeOldLogsUseCase @Inject constructor(
    private val logRepository: LogRepository
) {
    /**
     * Executes the purge operation.
     *
     * @param retentionDays Number of days to retain logs.
     * @return [Result.Success] on success, [Result.Error] on failure.
     */
    suspend operator fun invoke(retentionDays: Int): Result<Unit> {
        return logRepository.purgeLogs(retentionDays)
    }
}
