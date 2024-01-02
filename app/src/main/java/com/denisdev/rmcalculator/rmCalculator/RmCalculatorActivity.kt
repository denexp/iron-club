package com.denisdev.rmcalculator.rmCalculator

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
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl.Companion.CONSISTENT_RESULT_LIMIT
import com.denisdev.rmcalculator.R
import com.denisdev.rmcalculator.Utils.formatRound
import com.denisdev.rmcalculator.base.AppTheme
import com.denisdev.rmcalculator.base.BaseActivity
import com.denisdev.rmcalculator.components.DropDown
import com.denisdev.rmcalculator.components.asResource
import com.denisdev.rmcalculator.components.take
import com.denisdev.rmcalculator.rmCalculator.RmUiData.Companion.DEFAULT_AUTHOR
import com.denisdev.rmcalculator.rmCalculator.RmUiData.Companion.DEFAULT_WEIGHT_UNIT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@AndroidEntryPoint
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
    val previewViewModel = RmCalculatorViewModel(RmUiData(), object: GetRm {
        override fun invoke(params: GetRm.Params?): Flow<Result<RM>> {
            return flow { emit(runCatching { RM(Author.Brzycki, Weight(0f, WeightUnit.Kg)) }) }
        }
    })
    AppTheme {
        RMCalculatorView(previewViewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RMCalculatorView(viewModel: RmCalculatorViewModel = hiltViewModel()) {
    val (weight, onWeight) = rememberSaveable { mutableStateOf("") }
    val (reps, onReps) = rememberSaveable { mutableStateOf("") }
    val (weightUnit, onUnit) = rememberSaveable { mutableStateOf(DEFAULT_WEIGHT_UNIT) }
    val (author, onAuthor) = rememberSaveable { mutableStateOf(DEFAULT_AUTHOR) }
    val (autoFx, onAutoFx) = rememberSaveable { mutableStateOf(true) }

    val (weightRequester, repsRequester) = remember { FocusRequester.createRefs() }
    val data by viewModel.data(GetRm.Params(weight, reps, author, weightUnit, autoFx)).collectAsState()

    if (autoFx)
        onAuthor(data.author)

    Column(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 4.dp)
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
            weightRequester,
            repsRequester,
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
                             weightRequester: FocusRequester,
                             repsRequester: FocusRequester,
                             weight: Pair<String, (String) -> Unit>,
                             reps: Pair<String, (String) -> Unit>,
                             weightUnit: Pair<WeightUnit, (WeightUnit) -> Unit>,
                             author: Pair<Author, (Author) -> Unit>,
                             autoFx: Pair<Boolean, (Boolean) -> Unit>
) {
    Column(modifier, verticalArrangement = Arrangement.Bottom) {
        MoreOptionsSection(weightUnit, author, autoFx, false)
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            RmTextField(this, R.string.weight.asResource(), weight, 0.6f,
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                weightRequester,
                "WeightTextField",
                weight.first.isNotEmpty().take {
                    ClearButton(weight.second, R.string.description_weight_clear_button.asResource())
                }
            )
            RmTextField(this, R.string.reps.asResource(), reps, 0.4f,
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                repsRequester,
                "RepsTextField",
                reps.first.isNotEmpty().take {
                    ClearButton(reps.second, R.string.description_reps_clear_button.asResource())
                }
            )
        }
        if ((reps.first.toIntOrNull() ?: 0) > CONSISTENT_RESULT_LIMIT)
            Text(modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                text = R.string.less_reliable_results.asResource(CONSISTENT_RESULT_LIMIT + 1),
                style = TextStyle(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.End)
    }
    LaunchedEffect(Unit) { weightRequester.requestFocus() }
}

@Composable
private fun RmTextField(scope: RowScope, label: String, pair: Pair<String, (String) -> Unit>, weight: Float,
                        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                        focusRequester: FocusRequester,
                        testTag: String,
                        trailingIcon: @Composable (() -> Unit)? = null) {
    with(scope) {
        OutlinedTextField(value = pair.first, label = {
            Text(
                text = label, style = TextStyle(fontSize = 20.sp),
                overflow = TextOverflow.Ellipsis, maxLines = 1
            )
        }, onValueChange = pair.second, modifier = Modifier
            .focusRequester(focusRequester)
            .weight(weight)
            .testTag(testTag),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            trailingIcon = trailingIcon
        )
    }
}

@Composable
private fun ClearButton(
    action: (String) -> Unit,
    description: String
) {
    Icon(Icons.Rounded.Clear,
        description,
        modifier = Modifier
            .clickable {
                action(String())
            }
            .testTag("ClearButton"))
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