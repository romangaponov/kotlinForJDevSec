import org.junit.Assert
import org.junit.Test
import rational.toRational

class TestRationals {
private fun testInRange(element: String, start: String, end: String, expected: Boolean = true) {
    Assert.assertEquals("Wrong result for $element in $start..$end",
            expected, element.toRational() in start.toRational()..end.toRational())
}

@Test
fun test4InRange3() = testInRange("20395802948019459839003802001190283020/" +
        "32493205934869548609023910932454365628", "1/2", "2/3")

}