package com.denisdev.domain.usecases

abstract class UseCaseWith<in Params, out Value: Any> {
    abstract suspend fun execute(params: Params): Result<Value>
}

abstract class UseCase<out Value: Any> {
    abstract suspend fun execute(): Result<Value>
}
