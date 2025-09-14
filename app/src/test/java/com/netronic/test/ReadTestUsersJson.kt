package com.netronic.test

import java.nio.charset.StandardCharsets
import java.io.File
import java.io.InputStream

object ReadTestUsersJson {
    private val TEST_USERS_JSON_FILE = "/randomuser.json"
    fun load(): String {
        val stream = requireNotNull(javaClass.getResourceAsStream(TEST_USERS_JSON_FILE)) {
            "Test file not found: $TEST_USERS_JSON_FILE"
        }
        return stream.readBytes().toString(StandardCharsets.UTF_8)
    }
}