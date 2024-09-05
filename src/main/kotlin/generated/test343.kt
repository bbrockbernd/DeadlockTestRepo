/* 
{"deadlock":true,"nFunctions":5,"nCoroutines":2,"nChannels":4,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 5 different functions
- 4 different channels
- 2 different coroutines
- 5 different classes

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
package org.example.generated.test343
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ClassA(val ch1: Channel<Int>)
class ClassB(val ch2: Channel<Int>)
class ClassC(val ch3: Channel<Int>)
class ClassD(val ch4: Channel<Int>)
class ClassE(
    val ch1: Channel<Int>,
    val ch2: Channel<Int>, 
    val ch3: Channel<Int>, 
    val ch4: Channel<Int>
)

fun function1(ch1: Channel<Int>, ch2: Channel<Int>) {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            ch1.send(1)
            ch1.receive()
        }
        scope.launch {
            ch2.send(2)
            ch2.receive() // STUCK
        }
    }
}

fun function2(ch3: Channel<Int>, ch4: Channel<Int>) {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            ch3.send(3) // STUCK
            ch3.receive()
        }
        scope.launch {
            ch4.send(4) // STUCK
            ch4.receive()
        }
    }
}

fun function3(ch1: Channel<Int>, ch3: Channel<Int>) {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            ch1.receive()
            ch3.send(5)
        }
    }
}

fun function4(ch2: Channel<Int>, ch4: Channel<Int>) {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            ch2.receive()
            ch4.send(6)
        }
    }
}

fun function5(classE: ClassE) {
    runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            function1(classE.ch1, classE.ch2)
        }
        scope.launch {
            function2(classE.ch3, classE.ch4)
        }
    }
}

fun main(): Unit{
    runBlocking {
        val ch1 = Channel<Int>()
        val ch2 = Channel<Int>()
        val ch3 = Channel<Int>()
        val ch4 = Channel<Int>()

        val classA = ClassA(ch1)
        val classB = ClassB(ch2)
        val classC = ClassC(ch3)
        val classD = ClassD(ch4)
        val classE = ClassE(ch1, ch2, ch3, ch4)

        launch {
            function1(classA.ch1, classB.ch2)
        }
        launch {
            function2(classC.ch3, classD.ch4)
        }
        launch {
            function3(classA.ch1, classC.ch3)
        }
        launch {
            function4(classB.ch2, classD.ch4)
        }
        launch {
            function5(classE)
        }
    }
}