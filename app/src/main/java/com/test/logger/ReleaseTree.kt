package com.test.logger

import timber.log.Timber

class ReleaseTree() : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
    }
}