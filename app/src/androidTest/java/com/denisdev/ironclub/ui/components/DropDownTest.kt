package com.denisdev.ironclub.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.ironclub.R
import com.denisdev.ironclub.assertContains
import com.denisdev.ironclub.base.AppTheme
import com.denisdev.ironclub.components.DropDown
import com.denisdev.ironclub.components.asResource
import com.denisdev.ironclub.performSelectDropDownItemByTag
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DropDownTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val list = Author.values().map { it.name }.toTypedArray()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            AppTheme {
                var text by remember { mutableStateOf(list.first()) }
                DropDown(
                    androidx.compose.ui.Modifier,
                    true,
                    TextStyle(fontSize = 20.sp),
                    false,
                    list,
                    text,
                    { text = it },
                    R.string.formula.asResource()
                )
            }
        }
    }

    @Test
    fun displayDropDownMenu() {
        composeTestRule.onNodeWithTag("DropDownField")
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithTag("DropdownMenu")
            .assertIsDisplayed()
    }

    @Test
    fun equalsList() {
        composeTestRule.onNodeWithTag("DropDownField")
            .performClick()

        composeTestRule.onNodeWithTag("DropdownMenu")
            .onChildren()
            .assertCountEquals(list.count())
            .assertContains(list.toList())
    }

    @Test
    fun setMenuItem() {
        val expected = Author.Brzycki.name

        composeTestRule.performSelectDropDownItemByTag("DropDownField", expected)

        composeTestRule.onNodeWithTag("DropDownField")
            .assert(hasText(expected))
    }
}