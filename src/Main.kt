import sun.security.util.BitArray

fun main(args: Array<String>) {
    println("№ варианта: 7")
    println("Информационный вектор: 10101010011")
    println("Код: X [15,11]")
    println("Обнаруживающая способность кода")
    println()

    val originalMessage = Message(intToBitArray(11, 0b10101010011))
    val hammingLength = 15

    val table = ArrayList<ResultingTableRow>()
    for (i in 0..hammingLength) {
        table.add(ResultingTableRow())
    }

    for(errorBitVector in 0 until Math.pow(2.0, hammingLength.toDouble()).toInt()) {
        val result = transmitWithErrorVector(originalMessage, errorBitVector)
        val errorBitVectorArray = intToBitArray(hammingLength, errorBitVector)
        val countOfErrorBits = errorBitVectorArray.toBooleanArray().count { it }
        val currentRow = table[countOfErrorBits]
        currentRow.combinations++
        if (result.first > -1) {
            currentRow.numberOfDetections++
            if (result.second.bits == originalMessage.bits) {
                currentRow.numberOfFixes++
            }
        }
    }

    println(" i |    C |   No |   Nk |       Co |       Ck")
    table.forEachIndexed { index, row ->
        println(
                "%2d | %4d | %4d | %4d | %2f | %2f"
                .format(
                        index,
                        row.combinations,
                        row.numberOfDetections,
                        row.numberOfFixes,
                        row.ratioOfDetections,
                        row.ratioOfFixes
                )
        )
    }
}

fun transmitWithErrorVector(originalMessage: Message, errorSet: Int): Pair<Int, Message> {
    val encoder = HammingEncoder()
    val encodedMessage = encoder.encode(originalMessage)

    val errorMaker = ErrorMaker()
    val messageWithErrors = errorMaker.makeErrors(
            encodedMessage,
            intToBitArray(15, errorSet)
    )

    val receiver = HammingReceiver()
    val errorBitIndex = receiver.findError(messageWithErrors)
    val fixedMessage = receiver.fixBits(messageWithErrors, errorBitIndex)
    val informationBits = receiver.selectInformationBits(fixedMessage)

    return Pair(errorBitIndex, informationBits)
}

fun intToBitArray(length: Int, input: Int): BitArray {
    val bits = BitArray(length)
    for (i in 0 until length) {
        bits[length - i - 1] = (input and (1 shl i)) != 0
    }
    return bits
}