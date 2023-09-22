package com.comunidadedevspace.taskbeats

import kotlin.random.Random

interface MyCountRepository {

    fun sum(): Int
    fun sub(p1:Int, p2:Int): Int

}
class MyCountRepositoryImpl(
    private val numbersProvider: MyNumbersProvider
): MyCountRepository {
    override fun sum(): Int {
        val p1 = numbersProvider.getNumber()
        val p2 = numbersProvider.getNumber()
        return p1 + p2
    }

    override fun sub(p1: Int, p2: Int): Int {
        return p1 - p2
    }
}
interface MyNumbersProvider {

    fun getNumber(): Int
}
class MyNumbersProviderImpl: MyNumbersProvider {
    override fun getNumber(): Int {
        return Random.nextInt(from = 0, until = 100)
    }
}class FakeMyNumbersProviderImpl: MyNumbersProvider {

    var p1 = 0
    override fun getNumber(): Int {
        return p1
    }
}