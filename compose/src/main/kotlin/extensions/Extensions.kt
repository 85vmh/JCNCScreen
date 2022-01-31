package extensions

import androidx.compose.foundation.gestures.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindovercnc.base.data.TextLines
import com.mindovercnc.linuxcnc.IntList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

fun Double.trimDigits(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).toString()
}

fun Double.stripZeros(maxDigits: Int = 3): String {
    return BigDecimal(this).setScale(maxDigits, RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString()
}

@Composable
fun Modifier.draggableScroll(
    scrollState: ScrollableState,
    scope: CoroutineScope,
    orientation: Orientation = Orientation.Vertical
): Modifier {
    return draggable(rememberDraggableState { delta ->
        scope.launch {
            scrollState.scrollBy(-delta)
        }
    }, orientation = orientation)
}

fun File.readLines(scope: CoroutineScope): TextLines {
    var byteBufferSize: Int
    val byteBuffer = RandomAccessFile(this, "r").use { file ->
        byteBufferSize = file.length().toInt()
        file.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
    }

    val lineStartPositions = IntList()

    var size = 0

    val refreshJob = scope.launch {
        delay(100)
        size = lineStartPositions.size
        while (true) {
            delay(1000)
            size = lineStartPositions.size
        }
    }

    scope.launch(Dispatchers.IO) {
        readLinePositions(lineStartPositions)
        refreshJob.cancel()
        size = lineStartPositions.size
    }

    return object : TextLines {
        override val size get() = size

        override fun get(index: Int): String {
            val startPosition = lineStartPositions[index]
            val length = if (index + 1 < size) lineStartPositions[index + 1] - startPosition else
                byteBufferSize - startPosition
            // Only JDK since 13 has slice() method we need, so do ugly for now.
            byteBuffer.position(startPosition)
            val slice = byteBuffer.slice()
            slice.limit(length)
            return StandardCharsets.UTF_8.decode(slice).toString()
        }
    }
}

private fun File.readLinePositions(
    starts: IntList
) {
    require(length() <= Int.MAX_VALUE) {
        "Files with size over ${Int.MAX_VALUE} aren't supported"
    }

    val averageLineLength = 200
    starts.clear(length().toInt() / averageLineLength)

    try {
        FileInputStream(this@readLinePositions).use {
            val channel = it.channel
            val ib = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size()
            )
            var isBeginOfLine = true
            var position = 0L
            while (ib.hasRemaining()) {
                val byte = ib.get()
                if (isBeginOfLine) {
                    starts.add(position.toInt())
                }
                isBeginOfLine = byte.toInt().toChar() == '\n'
                position++
            }
            channel.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        starts.clear(1)
        starts.add(0)
    }

    starts.compact()
}

/**
 * Compact version of List<Int> (without unboxing Int and using IntArray under the hood)
 */
class IntList(initialCapacity: Int = 16) {
    @Volatile
    private var array = IntArray(initialCapacity)

    @Volatile
    var size: Int = 0
        private set

    fun clear(capacity: Int) {
        array = IntArray(capacity)
        size = 0
    }

    fun add(value: Int) {
        if (size == array.size) {
            doubleCapacity()
        }
        array[size++] = value
    }

    operator fun get(index: Int) = array[index]

    private fun doubleCapacity() {
        val newArray = IntArray(array.size * 2 + 1)
        System.arraycopy(array, 0, newArray, 0, size)
        array = newArray
    }

    fun compact() {
        array = array.copyOfRange(0, size)
    }
}