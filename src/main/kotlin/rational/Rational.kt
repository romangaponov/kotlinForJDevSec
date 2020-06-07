package rational

import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    val number = 5 divBy 6

    println(number.equals(sum))

    val difference: Rational = half - third
    val number1 = 1 divBy 6

    println(number1.equals(difference))

    val product: Rational = half * third
    println(number1.equals(product))

    val quotient: Rational = half / third
    val number2 = 3 divBy 2

    println(number2.equals(quotient))

    val negation: Rational = -half
    val number3 = -1 divBy 2

    println(number3.equals(negation))

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

public infix fun BigInteger.divBy(toBigInteger: BigInteger): Rational {
    return Rational(this, toBigInteger)
}

public infix fun Long.divBy(l: Long): Rational {
    return Rational(this.toBigInteger(), l.toBigInteger())
}

public fun String.toRational(): Rational {
    val number = split("/")

    when {
        number.size == 1 -> return Rational(number[0].toBigInteger(), 1.toBigInteger())
        else -> return Rational(number[0].toBigInteger(), number[1].toBigInteger())
    }
}

class Rational(var numerator: BigInteger, var denominator: BigInteger): Comparable<Rational> {
    init {
        if (denominator == BigInteger.ZERO) {
            throw IllegalArgumentException()
        }
        val pair = this.toNormalized()
        numerator = pair.first
        denominator = pair.second
    }

    private fun toNormalized() : Pair<BigInteger, BigInteger> {
        val gcd = numerator.gcd(denominator)
        if (denominator < 0.toBigInteger()) {
            numerator *= (-1).toBigInteger()
            denominator *= (-1).toBigInteger()
        }
        return numerator / gcd to denominator / gcd
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Rational

        val thisN = simplify(this)
        val otherN = simplify(other)

        return thisN.numerator.toDouble().div(thisN.denominator.toDouble()) == (otherN.numerator.toDouble().div(otherN.denominator.toDouble()))
    }

    private fun normalize(numerator: BigInteger, denominator: BigInteger): Rational {
        val finishGcd = denominator.gcd(numerator)
        val normalizeDenominator = denominator.divide(finishGcd)
        println("normalize denomitor -> $normalizeDenominator")
        val normalizeNumerator = numerator.divide(finishGcd)
        return Rational(normalizeNumerator, normalizeDenominator)
    }

    private fun getCommonDenominator(third: Rational): BigInteger {
        val times = denominator.times(third.denominator)
        println("common denominator -> $times")
        return times
    }

    operator fun plus(third: Rational): Rational {
        val newDenominator = getCommonDenominator(third)
        println("newDenominator -> $newDenominator")
        val newNumerator1 = newDenominator.divide(denominator).times(numerator)
        val newNumerator2 = newDenominator.divide(third.denominator).times(third.numerator)
        val resultNumerator = newNumerator1.plus(newNumerator2)
        println("resultNimerator -> $resultNumerator")
        return normalize(resultNumerator, newDenominator)
    }

    operator fun minus(third: Rational): Rational {
        val newDenominator = getCommonDenominator(third)
        val newNumerator1 = newDenominator.divide(denominator).times(numerator)
        val newNumerator2 = newDenominator.divide(third.denominator).times(third.numerator)
        val resultNumerator = newNumerator1.minus(newNumerator2)
        return normalize(resultNumerator, newDenominator)
    }

    operator fun times(third: Rational): Rational {
        return normalize(numerator.times(third.numerator),
                denominator.times(third.denominator))
    }

    operator fun div(third: Rational): Rational {
        val newDenominator = denominator.times(third.numerator)
        val newNumerator = numerator.times(third.denominator)
        return normalize(newNumerator, newDenominator)
    }

    operator fun unaryMinus(): Rational {
        return normalize(numerator.unaryMinus(), denominator.unaryMinus())
    }


    override fun compareTo(other : Rational) : Int = (this.numerator * other.denominator).compareTo(this.denominator * other.numerator)

    fun formatRational(r: Rational): String = r.numerator.toString() + "/" + r.denominator.toString()
    fun simplify(r1: Rational): Rational {
        val hcf = hcf(r1.numerator, r1.denominator).abs()
        return Rational(r1.numerator.div(hcf), r1.denominator.div(hcf))
    }

    fun hcf(n1: BigInteger, n2: BigInteger): BigInteger =
            if (n2 != 0.toBigInteger()) hcf(n2, n1 % n2) else n1

    override fun toString(): String {
        return when {
            denominator == 1.toBigInteger() || numerator.rem(denominator) == 0.toBigInteger() -> numerator.div(denominator).toString()
            else -> {
                val r = simplify(this)

                if (r.denominator < 0.toBigInteger() || (r.numerator < 0.toBigInteger() && r.denominator < 0.toBigInteger())) {
                    formatRational(Rational(r.numerator.negate(), r.denominator.negate()))
                } else {
                    formatRational(Rational(r.numerator, r.denominator))
                }
            }
        }
    }


}

public infix fun Int.divBy(i: Int): Rational {
    val rational = Rational(this.toBigInteger(), i.toBigInteger())
    println("divby -> $rational")
    return rational
}

