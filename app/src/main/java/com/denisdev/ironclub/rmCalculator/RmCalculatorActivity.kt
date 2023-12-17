package com.denisdev.ironclub.rmCalculator

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRM
import com.denisdev.domain.usecases.rmcalculator.GetRM.Companion.CONSISTENT_RESULT_LIMIT
import com.denisdev.ironclub.R
import com.denisdev.ironclub.Utils.formatRound
import com.denisdev.ironclub.base.AppTheme
import com.denisdev.ironclub.base.BaseActivity
import com.denisdev.ironclub.components.DropDown
import com.denisdev.ironclub.components.asResource
import com.denisdev.ironclub.components.take
import com.denisdev.ironclub.rmCalculator.RmUiData.Companion.DEFAULT_AUTHOR
import com.denisdev.ironclub.rmCalculator.RmUiData.Companion.DEFAULT_WEIGHT_UNIT

class RmCalculatorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            AppTheme {
                RMCalculatorView()
            }
        }
    }

}

@Preview(device = "spec:width=411dp,height=891dp", showSystemUi = true)
@Composable
fun RMCalculatorPreview() {
    AppTheme {
        RMCalculatorView()
    }
}

@Composable
fun RMCalculatorView(viewModel: RmCalculatorViewModel = viewModel<RmCalculatorViewModel>()) {
    val (weight, onWeight) = rememberSaveable { mutableStateOf("") }
    val (reps, onReps) = rememberSaveable { mutableStateOf("") }
    val (weightUnit, onUnit) = rememberSaveable { mutableStateOf(DEFAULT_WEIGHT_UNIT) }
    val (author, onAuthor) = rememberSaveable { mutableStateOf(DEFAULT_AUTHOR) }
    val (autoFx, onAutoFx) = rememberSaveable { mutableStateOf(true) }

    val data by viewModel.data(GetRM.Params(weight, reps, author, weightUnit, autoFx)).collectAsState()

    if (autoFx)
        onAuthor(data.author)

    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .padding(20.dp)
        .fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = data.rm.formatRound(), style = TextStyle(fontSize = 70.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.testTag("RM"))
                Text(text = weightUnit.name, style = TextStyle(fontSize = 30.sp),
                    color = MaterialTheme.colorScheme.onBackground)
            }
            if (data.rm > 0)
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    PercentList(data.rm)
                }
        }

        InputFormSection(
            Modifier
                .imePadding()
                .fillMaxWidth(),
            weight to onWeight,
            reps to onReps,
            weightUnit to onUnit,
            author to onAuthor,
            autoFx to onAutoFx
        )
    }
}

@Composable
fun PercentList(rm: Float) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            GeneratePercents(rm, 40..95, 5)
        }
        Column {
            GeneratePercents(rm, 10..35, 5)
            GeneratePercents(rm, 1..5, 1)
            PercentItem("0.5%", ((rm * 0.5f) / 100).formatRound())
        }
    }
}

fun getPercentList(rm: Float, range: IntRange, chunk: Int) =  (range).reversed().chunked(chunk).mapNotNull { it.firstOrNull() }
    .map { i ->
        ("$i   %".takeIf { i < 10 } ?: "$i %") to ((rm * i) / 100).formatRound()
    }

@Composable
private fun GeneratePercents(rm: Float, range: IntRange, chunk: Int) {
    getPercentList(rm, range, chunk).forEach {
        PercentItem(it.first, it.second)
    }
}

@Composable
private fun PercentItem(percent: String, nRm: String) {
    Row {
        Text(
            text = R.string.percent_and_weight.asResource(percent, nRm), style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.testTag("PercentItem")
        )
    }
}

@Composable
private fun InputFormSection(modifier: Modifier = Modifier,
                             weight: Pair<String, (String) -> Unit>,
                             reps: Pair<String, (String) -> Unit>,
                             weightUnit: Pair<WeightUnit, (WeightUnit) -> Unit>,
                             author: Pair<Author, (Author) -> Unit>,
                             autoFx: Pair<Boolean, (Boolean) -> Unit>
) {
    Column(modifier, verticalArrangement = Arrangement.Bottom) {
        MoreOptionsSection(weightUnit, author, autoFx, false)
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            OutlinedTextField(value = weight.first, label = {
                Text(text = R.string.weight.asResource(), style = TextStyle(fontSize = 20.sp))
            }, onValueChange = weight.second, modifier = Modifier
                .weight(0.6f)
                .testTag("WeightTextField"),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                trailingIcon = weight.first.isNotEmpty().take {
                    Icon(Icons.Rounded.Clear, null, modifier = Modifier
                        .clickable {
                            weight.second(String())
                        }
                        .testTag("WeightClearButton"))
                }
            )
            OutlinedTextField(value = reps.first, label = {
                Text(text = R.string.reps.asResource(), style = TextStyle(fontSize = 20.sp),
                    overflow = TextOverflow.Ellipsis, maxLines = 1)
            }, onValueChange = reps.second, modifier = Modifier
                .weight(0.4f)
                .testTag("RepsTextField"),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = reps.first.isNotEmpty().take {
                    Icon(Icons.Rounded.Clear, null, modifier = Modifier
                        .clickable {
                            reps.second(String())
                        }
                        .testTag("RepsClearButton"))
                }
            )
        }
        if ((reps.first.toIntOrNull() ?: 0) > CONSISTENT_RESULT_LIMIT)
            Text(modifier = Modifier.fillMaxWidth(),
                text = R.string.less_reliable_results.asResource(CONSISTENT_RESULT_LIMIT + 1),
                style = TextStyle(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.End)
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MoreOptionsSection(weightUnit: Pair<WeightUnit, (WeightUnit) -> Unit>,
                               author: Pair<Author, (Author) -> Unit>,
                               autoFx: Pair<Boolean, (Boolean) -> Unit>,
                               expanded: Boolean) {
    var visible by rememberSaveable { mutableStateOf(expanded) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End.takeIf { !visible } ?: Arrangement.SpaceBetween
    ) {
        if (visible) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = R.string.auto_fx.asResource(), style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary)
                Switch(checked = autoFx.first, onCheckedChange = autoFx.second, modifier = Modifier.testTag("AutoFxSwitch"))
            }
        }
        TextButton(modifier = Modifier.testTag("MoreOptionsButton"), onClick = { visible = !visible }, contentPadding = PaddingValues()) {
            Text(text = R.string.more_options.asResource(), style = TextStyle(fontSize = 20.sp))
            ExposedDropdownMenuDefaults.TrailingIcon(!visible)
        }
    }
    AnimatedVisibility(visible = visible) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.testTag("MoreOptionsDropDowns")) {
            DropDown(
                Modifier
                    .weight(0.7f)
                    .padding(vertical = 12.dp)
                    .wrapContentWidth(),
                !autoFx.first,
                TextStyle(fontSize = 20.sp),
                false,
                Author.values().map { it.name }.toTypedArray(),
                author.first.name,
                { author.second(Author.valueOf(it)) },
                R.string.formula.asResource()
            )

            DropDown(
                Modifier
                    .padding(vertical = 12.dp)
                    .weight(0.3f),
                true,
                TextStyle(fontSize = 20.sp),
                false,
                WeightUnit.values().map { it.name }.toTypedArray(),
                weightUnit.first.name,
                { weightUnit.second(WeightUnit.valueOf(it)) },
                R.string.unit.asResource()
            )
        }

    }
}