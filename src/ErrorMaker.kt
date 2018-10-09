import sun.security.util.BitArray

class ErrorMaker {
    fun makeErrors(input: Message, where: BitArray): Message {
        val copy = input.bits.clone() as BitArray
        where.toBooleanArray().forEachIndexed { index, errorBit -> if (errorBit) copy[index] = !copy[index] }
        return Message(copy)
    }
}