package com.ab.migotest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object TestUtil {

    fun rvItemCountMatcher(matcherItemCount: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with list size: $matcherItemCount")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                return matcherItemCount == recyclerView.adapter!!.itemCount
            }
        }
    }

    fun rvViewTypeMatcher(position: Int, matcherViewType: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("position($position) with viewType: $matcherViewType")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                return matcherViewType == recyclerView.adapter!!.getItemViewType(position)
            }
        }
    }

    fun rvAtPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}