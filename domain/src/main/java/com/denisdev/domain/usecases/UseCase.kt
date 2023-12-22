package com.denisdev.domain.usecases

interface UseCase<in R, out T: Any> {
    operator fun invoke(params: R? = null): T
}