package com.denisdev.domain.model

interface Formula<K, V> {
    val author: K
    fun calculate(): V
}