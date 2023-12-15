package com.denisdev.ironclub.base

import android.os.Bundle
import androidx.activity.ComponentActivity

abstract class BaseActivity: ComponentActivity() {

    //inline fun<reified T: ViewModel> viewModel(owner: ViewModelStoreOwner = this) = lazy { ViewModelProvider(owner)[T::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}