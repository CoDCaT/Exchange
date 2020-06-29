package com.test.logger

import timber.log.Timber

class DebugTree() : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
    }
}