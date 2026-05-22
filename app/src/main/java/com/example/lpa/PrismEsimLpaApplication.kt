package com.example.lpa

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base [Application] class for PrismEsimLpa.
 *
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation and
 * set up the application-level dependency injection component.
 */
@HiltAndroidApp
class PrismEsimLpaApplication : Application()
