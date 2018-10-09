import sun.security.util.BitArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.log2

class HammingReceiver {
    fun findError(message: Message): Int {
        assert(message.bits.length() >= 2)

        val syndrome = BitArray(log2(message.bits.length().toDouble()).toInt() + 1)

        for(i in 0 until message.bits.length()) {
            var tmp = i + 1
            var pos = 0
            while(tmp > 0) {
                val checkingBitRepresentsCurrent = tmp % 2 == 1
                if(checkingBitRepresentsCurrent && message.bits[i]) {
                    val representingCheckingBit = Math.pow(2.0, pos.toDouble()).toInt() - 1
                    val errorIndex = log2(representingCheckingBit.toDouble() + 1).toInt()
                    syndrome[errorIndex] = !syndrome[errorIndex]
                }
                pos++
                tmp /= 2
            }
        }

        return toInt(syndrome) - 1
    }

    fun selectInformationBits(message: Message): Message {
        val result = BitArray(message.bits.length() - log2(message.bits.length().toDouble()).toInt() - 1)

        var curOriginalPos = 0
        var representedCount = 1
        var curRepresented = 1

        for (curHammingPos in 0 until message.bits.length()) {
            val isChecking = ((curHammingPos) and (curHammingPos + 1) == 0)
            if (isChecking) {
                curRepresented = 0
                representedCount *= 2
            } else {
                result[curOriginalPos] = message.bits[curHammingPos]
                curRepresented++
                curOriginalPos++
            }
        }

        return Message(result)
    }

    fun fixBits(message: Message, errorIndex: Int): Message {
        assert(errorIndex >= -1 && errorIndex < message.bits.length())
        val copy = message.bits.clone() as BitArray
        if (errorIndex != -1) {
            copy[errorIndex] = !copy[errorIndex]
        }
        return Message(copy)
    }

    private fun toInt(bits: BitArray): Int
    {
        var result = 0
        var multiplier = 1
        bits.toBooleanArray().forEach { value ->
            if (value) result += multiplier
            multiplier *= 2
        }
        return result
    }
}