package com.denisdev.domain.model.rm

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight

data class RM(
    val author: Author,
    val weight: Weight
)