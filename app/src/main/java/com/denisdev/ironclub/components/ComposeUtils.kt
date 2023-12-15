package com.denisdev.ironclub.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

fun Boolean.take(block: @Composable (() -> Unit)) = if (this) block else null

@Composable
fun @receiver:StringRes Int.asResource(): String = stringResource(id = this)
@Composable
fun @receiver:StringRes Int.asResource(vararg formatArgs: Any): String = stringResource(id = this, formatArgs = formatArgs)