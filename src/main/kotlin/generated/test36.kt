/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":8,"nChannels":4,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 4 different channels
- 8 different coroutines
- 3 different classes

You ARE ALLOWED to use basic Kotlin constructs and coroutine primitives. A few examples are:
- functions and suspend functions
- dot qualified expressions
- class properties
- local variables
- unbuffered and buffered channels
- channel init, send and receive
- return values
- function composition (nested calls)
- runBlocking and launch builder
- coroutineScope

You ARE NOT ALLOWED to use more complex features like:
- joins
- async builder
- mutability
- nullability
- for (i in channel)
- flow
- lateinit
- lazyval
- inheritance
- lambdas with arguments
- mutexes 
*/
package org.example.generated.test36
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val c3: Channel<Int>, val c4: Channel<Int>) {
    suspend fun func1() {
        c3.send(1)
        c4.send(c3.receive())
    }

    suspend fun func2() {
        val value = c4.receive()
        c3.send(value)
    }
}

class B(val c1: Channel<Int>, val c2: Channel<Int>) {
    suspend fun func3() {
        c1.send(3)
        c2.send(c1.receive())
    }

    suspend fun func4() {
        val value = c2.receive()
        c1.send(value)
    }
}

class C(val c1: Channel<Int>, val c2: Channel<Int>, val c3: Channel<Int>, val c4: Channel<Int>) {
    private val a = A(c3, c4)
    private val b = B(c1, c2)
    
    suspend fun func5() {
        a.func1()
        b.func3()
    }

    suspend fun func6() {
        a.func2()
        b.func4()
    }

    suspend fun func7() {
        c4.send(c1.receive())
        c2.send(c3.receive())
    }

    suspend fun func8() {
        c1.send(c4.receive())
        c3.send(c2.receive())
    }
}

fun main(): Unit = runBlocking {
    val c1 = Channel<Int>()
    val c2 = Channel<Int>()
    val c3 = Channel<Int>()
    val c4 = Channel<Int>()
    
    val c = C(c1, c2, c3, c4)

    launch { c.func5() }
    launch { c.func6() }
    launch { c.func7() }
    launch { c.func8() }
    launch { c.a.func1() }
    launch { c.a.func2() }
    launch { c.b.func3() }
    launch { c.b.func4() }
}