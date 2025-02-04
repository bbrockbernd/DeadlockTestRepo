/* 
{"deadlock":false,"nFunctions":3,"nCoroutines":3,"nChannels":3,"nClasses":5}
I need to test a channel deadlock detection algorithm for Kotlin coroutines. 
I want you to create a test sample that DOES NOT contain a DEADLOCK.
The deadlock (if any) should arise from channels and coroutines.
I want the example to consist of:
- 3 different functions
- 3 different channels
- 3 different coroutines
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
package org.example.altered.test366
import org.example.altered.RunCheckerBase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

data class Task(val id: Int, val description: String)
data class Message(val content: String)
data class Result(val taskId: Int, val success: Boolean)

class Producer {
    suspend fun produceTasks(channel: Channel<Task>) {
        for (i in 1..5) {
            channel.send(Task(i, "Task #$i"))
        }
        channel.close()
    }
}

class Processor {
    suspend fun processTasks(taskChannel: Channel<Task>, resultChannel: Channel<Result>) {
        for (task in taskChannel) {
            resultChannel.send(Result(task.id, task.id % 2 == 0))
        }
        resultChannel.close()
    }
}

class Consumer {
    suspend fun consumeResults(resultChannel: Channel<Result>, messageChannel: Channel<Message>) {
        for (result in resultChannel) {
            val message = if (result.success) {
                "Task ${result.taskId} succeeded."
            } else {
                "Task ${result.taskId} failed."
            }
            messageChannel.send(Message(message))
        }
        messageChannel.close()
    }
}

fun initiatePipeline(producer: Producer, processor: Processor, consumer: Consumer) {
    val taskChannel = Channel<Task>()
    val resultChannel = Channel<Result>()
    val messageChannel = Channel<Message>()

    runBlocking {
        launch {
            producer.produceTasks(taskChannel)
        }
        launch {
            processor.processTasks(taskChannel, resultChannel)
        }
        launch {
            consumer.consumeResults(resultChannel, messageChannel)
        }
        for (message in messageChannel) {
            println(message.content)
        }
    }
}

fun main(): Unit{
    val producer = Producer()
    val processor = Processor()
    val consumer = Consumer()
    initiatePipeline(producer, processor, consumer)
}

class RunChecker366: RunCheckerBase() {
    override fun block() = runBlocking { main() }
}