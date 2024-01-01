package com.denisdev.rmcalculator.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.rmcalculator.R
import com.denisdev.rmcalculator.base.AppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DropDown(
    modifier: Modifier,
    enabled: Boolean,
    textStyle: TextStyle,
    expanded: Boolean,
    list: Array<String>,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    var show by remember { mutableStateOf(expanded) }

    ExposedDropdownMenuBox(
        modifier = modifier.testTag("DropDown"),
        expanded = show && enabled,
        onExpandedChange = {
            show = !show
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.testTag("DropDownField")
                .focusable(false)
                .menuAnchor(),
            enabled = enabled,
            readOnly = true,
            textStyle = textStyle,
            value = text,
            label = {
                Text(text = label, style = TextStyle(fontSize = 20.sp))
            },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = !show && enabled
                )
            }
        )
        ExposedDropdownMenu(
            expanded = show && enabled,
            onDismissRequest = { show = false },
            Modifier.testTag("DropdownMenu")
        ) {
            list.forEach { selectionOption ->
                DropdownMenuItem(
                    modifier = Modifier.testTag("DropdownMenuItem"),
                    text = {
                        Text(text = selectionOption)
                    },
                    onClick = {
                        onValueChange(selectionOption)
                        show = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun DropDownPreview() {
    val list = Author.values().map { it.name }.toTypedArray()
    var text by remember { mutableStateOf(list.first()) }
    AppTheme {
        DropDown(
            Modifier
                .padding(vertical = 12.dp)
                .wrapContentWidth(),
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