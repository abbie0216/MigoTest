package com.ab.migotest.viewaction

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import org.hamcrest.Matcher

class ClickOnButtonView(
    @IdRes private val id: Int
) : ViewAction {
    private var click = ViewActions.click()

    override fun getConstraints(): Matcher<View> {
        return click.constraints
    }

    override fun getDescription(): String {
        return "click on inner btn"
    }

    override fun perform(uiController: UiController, view: View) {
        click.perform(uiController, view.findViewById(id))
    }
}