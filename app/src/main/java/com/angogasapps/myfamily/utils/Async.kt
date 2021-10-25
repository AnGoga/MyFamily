package com.angogasapps.myfamily.utils

import kotlin.concurrent.thread

object Async {
    fun runInNewThread(block: () -> Unit): Thread {
        val thread = thread {
            block()
        }
        return thread
    }

}