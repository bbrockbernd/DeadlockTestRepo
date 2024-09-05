/* 
{"deadlock":true,"nFunctions":8,"nCoroutines":4,"nChannels":7,"nClasses":3}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 8 different functions
- 7 different channels
- 4 different coroutines
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
package org.example.generated.test91
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class A(val ch1: Channel<Int>, val ch2: Channel<Int>) {
    suspend fun sendToChannel1() {
        ch1.send(1)
    }

    suspend fun receiveFromChannel2() {
        println(ch2.receive())
    }
}

class B(val ch3: Channel<Int>, val ch4: Channel<Int>) {
    suspend fun sendToChannel3() {
        ch3.send(2)
    }

    suspend fun receiveFromChannel4() {
        println(ch4.receive())
    }
}

class C(val ch5: Channel<Int>, val ch6: Channel<Int>, val ch7: Channel<Int>) {
    suspend fun sendToChannel5() {
        ch5.send(3)
    }

    suspend fun receiveFromChannel6() {
        println(ch6.receive())
    }

    suspend fun sendToChannel7() {
        ch7.send(4)
    }
}

suspend fun function1(a: A) {
    a.sendToChannel1()
}

suspend fun function2(b: B) {
    b.sendToChannel3()
}

suspend fun function3(c: C) {
    c.sendToChannel5()
}

suspend fun function4(a: A, c: C) {
    a.receiveFromChannel2()
    c.receiveFromChannel6()
}

suspend fun function5(b: B, c: C) {
    b.receiveFromChannel4()
    c.sendToChannel7()
}

suspend fun function6(a: A, c: C) {
    a.sendToChannel1()
    c.receiveFromChannel6()
}

suspend fun function7(b: B, c: C) {
    b.sendToChannel3()
    c.sendToChannel7()
}

suspend fun function8(a: A, b: B) {
    a.receiveFromChannel2()
    b.receiveFromChannel4()
}

fun main(): Unit= runBlocking {
    val ch1 = Channel<Int>()
    val ch2 = Channel<Int>()
    val ch3 = Channel<Int>()
    val ch4 = Channel<Int>()
    val ch5 = Channel<Int>()
    val ch6 = Channel<Int>()
    val ch7 = Channel<Int>()

    val a = A(ch1, ch2)
    val b = B(ch3, ch4)
    val c = C(ch5, ch6, ch7)

    launch { function1(a) }
    launch { function2(b) }
    launch { function3(c) }

    launch { function4(a, c) } // Potential deadlock here
}