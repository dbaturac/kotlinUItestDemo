package com.dorukbatur.kotlinuitestdemogithubac


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        // Intent'leri doğrulayabilmek için başlatıyoruz
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    // RecyclerView'deki öğe sayısını kontrol etmek için yardımcı matcher
    private fun withItemCount(expectedCount: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description?) {
                description?.appendText("RecyclerView with item count: $expectedCount")
            }
            override fun matchesSafely(item: View?): Boolean {
                if (item !is RecyclerView) return false
                return item.adapter?.itemCount == expectedCount
            }
        }
    }

    @Test
    fun testRecyclerView_initialItemCount() {
        // Başlangıçta RecyclerView’de 5 öğe olmalı
        onView(withId(R.id.recyclerView)).check(matches(withItemCount(5)))
    }

    @Test
    fun testSearchFiltersList() {
        // "Apple" yazınca listenin filtrelendiğini kontrol et
        onView(withId(R.id.searchEditText)).perform(typeText("Apple"), closeSoftKeyboard())
        onView(withId(R.id.recyclerView)).check(matches(withItemCount(1)))
        onView(withText("Apple")).check(matches(isDisplayed()))
    }

    @Test
    fun testRefreshButtonResetsList() {
        // Önce arama yapıp listeyi filtrele
        onView(withId(R.id.searchEditText)).perform(typeText("Cherry"), closeSoftKeyboard())
        onView(withId(R.id.recyclerView)).check(matches(withItemCount(1)))

        // Sonrasında Refresh butonuna tıkla
        onView(withId(R.id.refreshButton)).perform(click())

        // Arama alanının temizlendiğini ve listenin sıfırlandığını kontrol et
        onView(withId(R.id.searchEditText)).check(matches(withText("")))
        onView(withId(R.id.recyclerView)).check(matches(withItemCount(5)))
    }

    @Test
    fun testClickingItemOpensDetailActivity() {
        // "Banana" öğesine tıkla
        onView(withText("Banana")).perform(click())

        // DetailActivity'nin açıldığını doğrula
        intended(hasComponent(DetailActivity::class.java.name))

        // DetailActivity içindeki başlık ve açıklamanın doğru olduğunu kontrol et
        onView(withId(R.id.detailTitle)).check(matches(withText("Banana")))
        onView(withId(R.id.detailDescription)).check(matches(withText("A ripe banana.")))
    }
}