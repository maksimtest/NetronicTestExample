package com.netronic.test.presentation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.netronic.test.ReadTestUsersJson
import com.netronic.test.di.NetworkModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(NetworkModule::class)
@RunWith(AndroidJUnit4::class)
class UsersFlowInstrumentedTest {
    @get:Rule(order = 0) val hilt = HiltAndroidRule(this)
    @get:Rule(order = 1) val compose = createAndroidComposeRule<MainActivity>()

    @Inject lateinit var server: MockWebServer

    @Before
    fun setup() {
        hilt.inject()
    }
    @Test
    fun listThenDetails() {
        val body = ReadTestUsersJson.load()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        compose.onNodeWithText("Users").assertExists()
    }

    @After fun shutdown() { server.shutdown() }
}
