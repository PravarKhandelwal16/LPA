package com.example.lpa.domain.usecase

import com.example.lpa.domain.models.LpaLogEntry
import com.example.lpa.domain.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe the stream of LPA operation logs.
 */
class ObserveLogsUseCase @Inject constructor(
    private val logRepository: LogRepository
) {
    /**
     * @return A live stream of [LpaLogEntry] objects, ordered newest-first.
     */
    operator fun invoke(): Flow<List<LpaLogEntry>> {
        return logRepository.observeLogs()
    }
}
