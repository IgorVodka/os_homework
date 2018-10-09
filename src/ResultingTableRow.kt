class ResultingTableRow {
    var combinations = 0
    var numberOfDetections = 0
    var numberOfFixes = 0

    val ratioOfDetections
        get() = numberOfDetections / combinations.toDouble()

    val ratioOfFixes
        get() = numberOfFixes / combinations.toDouble()
}