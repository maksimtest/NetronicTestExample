package com.netronic.test.logging

import android.util.Log

object AppLogger {
    private inline fun run(block: () -> Unit) {
        try {
            block()
        } catch (_: Throwable) {
        }
    }

    fun d(tag: String, msg: String) = run { Log.d(tag, msg) }
    fun w(tag: String, msg: String) = run { Log.w(tag, msg) }
    fun e(tag: String, msg: String, tr: Throwable? = null) =
        run { Log.e(tag, msg, tr) }
}