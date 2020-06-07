package taxipark

fun main(args: Array<String>) {

    val taxiPark =
            taxiPark(0..3, 0..11,
                    trip(3, listOf(9), duration = 4, distance = 26.0, discount = 0.3),
                    trip(0, listOf(7), duration = 16, distance = 34.0, discount = 0.2),
                    trip(2, listOf(9), duration = 19, distance = 16.0),
                    trip(1, listOf(4, 6, 3), duration = 0, distance = 3.0),
                    trip(3, listOf(6, 11), duration = 33, distance = 10.0),
                    trip(3, listOf(11, 9), duration = 20, distance = 22.0),
                    trip(1, listOf(3, 4), duration = 18, distance = 19.0),
                    trip(3, listOf(4, 7), duration = 0, distance = 31.0, discount = 0.3),
                    trip(0, listOf(8, 7), duration = 7, distance = 14.0),
                    trip(0, listOf(11, 7, 5, 8), duration = 4, distance = 1.0, discount = 0.4),
                    trip(3, listOf(4, 8, 1), duration = 35, distance = 2.0),
                    trip(3, listOf(1), duration = 35, distance = 30.0),
                    trip(2, listOf(6, 1), duration = 23, distance = 33.0),
                    trip(3, listOf(7, 6), duration = 38, distance = 9.0),
                    trip(1, listOf(3, 4, 5), duration = 2, distance = 34.0, discount = 0.2),
                    trip(1, listOf(4, 8, 7), duration = 5, distance = 31.0, discount = 0.1),
                    trip(0, listOf(11, 4, 6), duration = 15, distance = 2.0),
                    trip(3, listOf(9, 8, 6), duration = 24, distance = 17.0),
                    trip(3, listOf(0), duration = 37, distance = 3.0, discount = 0.1),
                    trip(1, listOf(5, 7), duration = 0, distance = 15.0, discount = 0.4))
    val paretoPrinciple = taxiPark.findFaithfulPassengers(0)

    println("result test -> $paretoPrinciple")


}
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    println("all taxipark.passengers -> $allPassengers")
    var toList = trips.flatMap { it.passengers }
            .toList()
            .groupBy { it.name }
            .filter { (k, v) ->
                v.size >= minTrips
            }.flatMap { it.value }
            .toSet()
    if(minTrips == 0){
        toList += allPassengers
    }
    return toList.sortedBy { it.name.replace("P-","").toInt()}.toSet()
}
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if(trips.isEmpty()) return false
    println("all taxipark.drivers -> $allDrivers")
    val summaryCost = trips.map { e -> e.cost }.reduce { ab, bc -> ab + bc }
    val percSumCost = summaryCost * 0.8
    var driversSet: MutableSet<Pair<Driver,Double>> = mutableSetOf<Pair<Driver,Double>>()
    var summatorsResults: MutableList<Double> = mutableListOf()
    var driversResult: MutableList<MutableSet<Pair<Driver,Double>>> = mutableListOf<MutableSet<Pair<Driver,Double>>>()
    println("summary cost-> $summaryCost")
    println("80% of summary cost -> $percSumCost")
    val allDriversSize = allDrivers.size
    var summator = 0.0
    val tripsGroupByDriver = trips.groupBy { it.driver }
            .map { (k, v) ->
                k to v.map { e -> e.cost }
                        .reduce { ab, bc -> ab + bc }
            }.sortedByDescending { e->e.second }.forEach { e ->
                println("element -> $e")
                summator += e.second
                driversSet.add(e)
                println("summator -> $summator")
                println("driversSet -> $driversSet")
                if (summator >= percSumCost) {
                    summatorsResults.add(summator)
                    driversResult.add(driversSet)
                    summator = 0.0
                    driversSet = mutableSetOf()
                }
            }

    println("summator result -> $summatorsResults")
    println("taxipark.drivers result -> $driversResult")

    if(driversResult.isEmpty())return false

    val endDriversSet = driversResult[0]

    val expectingDrivers = allDriversSize * 0.2
    println("expecting taxipark.drivers -> $expectingDrivers")
    if(endDriversSet.size<= expectingDrivers) {
        return true
    }else{
        val maxByPair = endDriversSet.maxBy { it.second }!!
        val maxBy = maxByPair.second
        if(maxBy >= percSumCost && 1 <= expectingDrivers){
            return true
        }else{
            endDriversSet.remove(maxByPair)
            val maxByPair2 = endDriversSet.maxBy { it.second }!!
            val finalResult = maxBy + maxByPair2.second
            if(finalResult >= percSumCost && 2 <= expectingDrivers){
                return true
            }
        }
    }
    return false
}

fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val toList = trips.map { el ->
        val duration = el.duration
        println("duration ->$duration")
        val stringDuration = duration.toString()
        val toCharArray = stringDuration.toCharArray()
        val lastIndex = toCharArray.lastIndex
        val startRange = stringDuration.substring(0 until lastIndex) + "0"
        val startRangeInt = startRange.toInt()
        val endRange = startRangeInt + 9
        println("startRange ->$startRange")
        el.duration to IntRange(startRangeInt, endRange)
    }.groupBy { it.second }.map { (k, v) -> k to v.size }.maxBy { it.second } ?: return null

    println(toList)
    return toList.first
}

fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    var groupBy = trips.groupBy { it.discount }.map { (k, v) ->
        println("key -> $k value-> $v")
        k to v.flatMap { it.passengers }
    }.toMap().map { (k, v) ->
        val groupBy = v.groupBy { it.name }.map { (k, v) -> k to v.size }

        println("_______________________")
        println("key -> $k value-> $v")
        k to groupBy
    }.sortedBy { it.first }

    val pair = groupBy[0]
    groupBy = groupBy.subList(1, groupBy.size)
    val flatMap = groupBy.flatMap {
        println("it in groupBy -> $it")
        it.second
    }
            .groupBy {
                println("it in groupBy2 -> $it")
                it.first
            }
            .map { (k, v) -> k to v.reduce { acc, pair -> Pair(acc.first, acc.second + pair.second) } }
            .map { it.second }

    println("groupBy -> $groupBy")

    println("flatMap -> $flatMap")
    val nullPair = pair.second.toMap()

    println("nullPair -> $nullPair")
    val filter = flatMap.filter {
        val i = nullPair[it.first]
        if (i != null) {
            println(it.first)
            println(it.second)
            it.second > i
        } else {
            true
        }
    }.map { (k, v) -> Passenger(k) }.sortedBy { it.name.replace("P-", "") }.toSet()
    println(filter)
    return filter
}




fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    var map2: Set<Passenger> = emptySet()
    val map = trips.groupBy { it.driver }.map { (k, v) ->
        k.name to v.flatMap { it.passengers }
    }.toMap()

    val list = map[driver.name]
    if (list != null) {
        map2 = list.groupBy { it.name }
                .toMap()
                .filterValues { it.size > 1 }
                .flatMap { it.value }
                //  .map { it.name.replace("P-","").toInt() }
                .toSet()
    }
    return map2
}

