package com.ab.migotest.view.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ab.migotest.R
import com.ab.migotest.TestUtil.rvAtPosition
import com.ab.migotest.TestUtil.rvItemCountMatcher
import com.ab.migotest.TestUtil.rvViewTypeMatcher
import com.ab.migotest.view.dialog.SelectDialogAdapter
import com.ab.migotest.view.main.PassAdapter.Companion.TYPE_PASS
import com.ab.migotest.view.main.PassAdapter.Companion.TYPE_SEPARATOR
import com.ab.migotest.viewaction.ClickOnButtonView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testIsListVisibleOnAppLaunch() {
        onView(withId(R.id.rv_pass)).check(matches(isDisplayed()))
    }

    @Test
    fun testAddDayPassAndActivate() {
        // check if list empty
        onView(withId(R.id.rv_pass)).check(matches(rvItemCountMatcher(0)))

        // click add day pass button
        onView(withId(R.id.btn_add_day_pass)).perform(click())

        // check if show select dialog
        onView(withId(R.id.cl_add_pass)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_select_title)).check(matches(withText("ADD DAY PASS")))

        // click first item and add
        onView(withId(R.id.rv_select_dialog)).perform(
            actionOnItemAtPosition<SelectDialogAdapter.SelectDialogViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.tv_select_confirm)).perform(click())

        // check if item and separator are added to list
        onView(withId(R.id.rv_pass)).check(matches(rvItemCountMatcher(2)))
        onView(withId(R.id.rv_pass)).check(matches(rvViewTypeMatcher(0, TYPE_SEPARATOR)))
        onView(withId(R.id.rv_pass)).check(matches(rvViewTypeMatcher(1, TYPE_PASS)))

        // click item
        onView(withId(R.id.rv_pass)).perform(actionOnItemAtPosition<PassViewHolder>(1, click()))

        // check if detail dialog shown
        onView(withId(R.id.cl_detail)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_status)).check(matches(withText("Pass Status: Unactivated")))

        pressBack()

        // check if btn text is BUY
        onView(withId(R.id.rv_pass)).check(matches(rvAtPosition(1, hasDescendant(withText("BUY")))))

        // click btn buy
        onView(withId(R.id.rv_pass)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ClickOnButtonView(R.id.btn_pass)
            )
        )

        // check if btn text change
        onView(withId(R.id.rv_pass)).check(
            matches(
                rvAtPosition(
                    1,
                    hasDescendant(withText("ACTIVATED"))
                )
            )
        )

        // click item
        onView(withId(R.id.rv_pass)).perform(actionOnItemAtPosition<PassViewHolder>(1, click()))

        // check if detail dialog shown with content changed
        onView(withId(R.id.cl_detail)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_status)).check(matches(withText("Pass Status: Activated")))
    }
}