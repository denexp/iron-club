package com.denisdev.rmcalculator

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

fun SemanticsNodeInteractionCollection.assertContains(list: List<String>): SemanticsNodeInteractionCollection {
    return apply {
        fetchSemanticsNodes().forEachIndexed { i, _ ->
            get(i).assert(hasText(list[i]))
        }
    }
}

fun SemanticsNodeInteractionCollection.filterIndexed(item: (Int, SemanticsNodeInteraction) -> Boolean): List<SemanticsNodeInteraction> {
    return List(fetchSemanticsNodes().size) { i -> get(i) }.filterIndexed { i, _ ->
        item(i, get(i))
    }
}

fun ComposeContentTestRule.assertExistText(vararg texts: String) {
    texts.forEach {
        onNodeWithText(it).assertExists()
    }
}

fun ComposeContentTestRule.performSelectDropDownItemByText(nodeWithText: String, item: String, tagName: String = "DropdownMenuItem"):
        SemanticsNodeInteraction {
    onNodeWithText(nodeWithText)
        .performClick()

    return onAllNodesWithTag(tagName)
        .filterToOne(hasText(item))
        .performClick()
}
fun ComposeContentTestRule.performSelectDropDownItemByTag(nodeWithTag: String, item: String, tagName: String = "DropdownMenuItem"):
        SemanticsNodeInteraction {
    onNodeWithTag(nodeWithTag)
        .performClick()

    return onAllNodesWithTag(tagName)
        .filterToOne(hasText(item))
        .performClick()
}

fun emptyText() = hasText(String())