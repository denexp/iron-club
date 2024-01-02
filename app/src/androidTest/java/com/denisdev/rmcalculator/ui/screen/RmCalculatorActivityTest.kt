package com.denisdev.rmcalculator.ui.screen

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.pressKey
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import com.denisdev.rmcalculator.R
import com.denisdev.rmcalculator.assertExistText
import com.denisdev.rmcalculator.emptyText
import com.denisdev.rmcalculator.performSelectDropDownItemByText
import com.denisdev.rmcalculator.rmCalculator.RmCalculatorActivity
import com.denisdev.rmcalculator.rmCalculator.RmUiData.Companion.DEFAULT_AUTHOR
import com.denisdev.rmcalculator.rmCalculator.RmUiData.Companion.DEFAULT_WEIGHT_UNIT
import org.junit.Rule
import org.junit.Test

class RmCalculatorActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<RmCalculatorActivity>()

    @Test
    fun initState() {
        composeTestRule.assertExistText("0", DEFAULT_WEIGHT_UNIT.name)

        composeTestRule.onNodeWithTag("WeightTextField")
            .assertIsFocused()

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
    fun textFieldImeNextTextField() {
        with(composeTestRule.activity) {
            composeTestRule.onNodeWithText(getString(R.string.weight))
                .performClick()
                .assertIsFocused()
                .performImeAction()

            composeTestRule.onNodeWithText(getString(R.string.reps))
                .assertIsFocused()
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
            composeTestRule.onNodeWithText(getString(R.string.less_reliable_results, GetRmImpl.CONSISTENT_RESULT_LIMIT + 1))
                .assertExists()
        }
    }

    @Test
    fun clearTextFieldsClearRm() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .performTextInput("20")

        composeTestRule.onAllNodesWithTag("ClearButton")
            .filterToOne(hasParent(hasTestTag("WeightTextField")))
            .assertExists()
            .performClick()
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("WeightTextField")
            .assert(emptyText())

        composeTestRule.onAllNodesWithTag("ClearButton")
            .filterToOne(hasParent(hasTestTag("RepsTextField")))
            .assertExists()
            .performClick()
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("RepsTextField")
            .assert(emptyText())

        composeTestRule.onNodeWithTag("RM")
            .assert(hasTextExactly("0"))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun setTextFieldsImeBackspaceClearRm() {
        composeTestRule.onNodeWithTag("WeightTextField")
            .performTextInput("100")
        composeTestRule.onNodeWithTag("RepsTextField")
            .apply {
                performTextInput("20")
                performKeyInput {
                    pressKey(Key.Backspace)
                    pressKey(Key.Backspace)
                }
            }.assert(emptyText())
        composeTestRule.onNodeWithTag("RM")
            .assert(hasTextExactly("0"))
    }
}