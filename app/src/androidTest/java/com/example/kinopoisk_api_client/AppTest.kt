package com.example.kinopoisk_api_client

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.kinopoisk_api_client.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun runApp() {
        ActivityScenario.launch(MainActivity::class.java)

        // Фильтрация
        Espresso.onView(withId(R.id.app_bar_search))
            .perform(ViewActions.typeText("fir"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withText("first"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Переход на экран фильма
        Espresso.onView(ViewMatchers.withText("first")).perform(ViewActions.click())

        // Проверка описания из мока
        Espresso.onView(withId(R.id.tvDesc))
            .check(ViewAssertions.matches(ViewMatchers.withText("desc")))
    }
}