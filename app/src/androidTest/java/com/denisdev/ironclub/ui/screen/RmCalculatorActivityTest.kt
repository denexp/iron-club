package com.denisdev.ironclub.ui.screen

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.ironclub.R
import com.denisdev.ironclub.assertExistText
import com.denisdev.ironclub.emptyText
import com.denisdev.ironclub.performSelectDropDownItemByText
import com.denisdev.ironclub.rmCalculator.RmCalculatorActivity
import com.denisdev.ironclub.rmCalculator.RmUiData.Companion.DEFAULT_AUTHOR
import com.denisdev.ironclub.rmCalculator.RmUiData.Companion.DEFAULT_WEIGHT_UNIT
import org.junit.Rule
import org.junit.Test

class RmCalculatorActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<RmCalculatorActivity>()

    @Test
    fun initState() {
        composeTestRule.assertExistText("0", DEFAULT_WEIGHT_UNIT.name)

        composeTestRule.onNodeWithTag("MoreOptionsDropDowns")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()
        composeTestRule.onNodeWithTag("MoreOptionsDropDowns")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .assertExists()
            .assertIsToggleable()
            .assertIsOn()

        composeTestRule.onAllNodesWithTag("DropDownField")
            .filterToOne(isNotEnabled())
            .assertExists()
            .assert(hasText(DEFAULT_AUTHOR.name))

        composeTestRule.onAllNodesWithTag("DropDownField")
            .filterToOne(hasText(DEFAULT_WEIGHT_UNIT.name))
            .assertExists()

        with(composeTestRule.activity) {
            composeTestRule.onNodeWithText(getString(R.string.weight))
                .assertExists()
                .performClick()
                .assertIsFocused()
                .assert(emptyText())

            composeTestRule.onNodeWithText(getString(R.string.reps))
                .assertExists()
                .performClick()
                .assertIsFocused()
                .assert(emptyText())

            composeTestRule.assertExistText(
                getString(R.string.more_options),
                getString(R.string.formula),
                getString(R.string.unit),
                getString(R.string.auto_fx)
            )
        }
    }

    @Test
    fun toggleToHideMoreOptions() {
        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()
        composeTestRule.onNodeWithTag("MoreOptionsDropDowns")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()

        composeTestRule.onNodeWithTag("MoreOptionsDropDowns")
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .assertDoesNotExist()
    }

    @Test
    fun setTextFieldsFailure() {
        composeTestRule.onNodeWithTag("WeightTextField").apply {
            performTextInput("100---")
        }.assert(hasText("100---"))
        composeTestRule.onNodeWithTag("RepsTextField").apply {
            performTextInput("8---")
        }.assert(hasText("8---"))

        composeTestRule.onNodeWithTag("RM")
            .assert(hasText("0"))
    }

    @Test
    fun setTextFieldsSuccessAutoFx() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("8")

        composeTestRule.onNodeWithTag("RM")
            .assert(hasText("124.16"))
    }

    @Test
    fun displayPercentList() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("8")

        composeTestRule.onAllNodesWithTag("PercentItem")
            .assertCountEquals(24)
    }

    @Test
    fun setTextFieldUnit() {
        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()

        composeTestRule.onAllNodesWithText(DEFAULT_WEIGHT_UNIT.name)
            .filterToOne(hasClickAction())
            .performClick()

        composeTestRule.onAllNodesWithTag("DropdownMenuItem")
            .filterToOne(hasText(WeightUnit.Lb.name))
            .performClick()

        composeTestRule.onAllNodesWithText(WeightUnit.Lb.name)
            .assertCountEquals(2)

    }
    @Test
    fun setTextFieldsSuccessNotAutoFx() {
        val expected = Author.MCGlothin.name

        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()
        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .performClick()

        composeTestRule.performSelectDropDownItemByText(DEFAULT_AUTHOR.name, expected)

        composeTestRule.onNodeWithText(expected).assertExists()

        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("8")

        composeTestRule.onNodeWithTag("RM")
            .assert(hasText("124.16"))
    }

    @Test
    fun setTextFieldsSuccessToggleAutoFx() {
        val expected = Author.MCGlothin.name

        composeTestRule.onNodeWithTag("MoreOptionsButton")
            .performClick()
        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .performClick()

        composeTestRule.performSelectDropDownItemByText(DEFAULT_AUTHOR.name, expected)

        composeTestRule.onNodeWithText(expected).assertExists()

        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("8")

        composeTestRule.onNodeWithTag("RM")
            .assert(hasText("124.16"))

        composeTestRule.onNodeWithTag("AutoFxSwitch")
            .performClick()

        composeTestRule.onNodeWithText(Author.Brzycki.name)
            .assertExists()
            .assertIsNotEnabled()

        composeTestRule.onNodeWithTag("RM")
            .assert(hasText("124.16"))
    }

    @Test
    fun setTextFieldsHighRepsMessage() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("20")

        with(composeTestRule.activity) {
            composeTestRule.onNodeWithText(getString(R.string.less_reliable_results, GetRM.CONSISTENT_RESULT_LIMIT + 1))
                .assertExists()
        }
    }

    @Test
    fun clearTextFields() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("20")

        composeTestRule.onNodeWithTag("WeightClearButton")
            .assertExists()
            .performClick()
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("WeightTextField")
            .assert(emptyText())

        composeTestRule.onNodeWithTag("RepsClearButton")
            .assertExists()
            .performClick()
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("RepsTextField")
            .assert(emptyText())
    }
}


