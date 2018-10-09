import sun.security.util.BitArray

class Message(private val bitArray: BitArray) {
    val bits: BitArray
        get() = this.bitArray
}