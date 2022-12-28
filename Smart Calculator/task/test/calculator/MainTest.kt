package calculator

import junit.framework.TestCase.*
import org.junit.Test
import java.util.regex.Pattern


class MainTest {
    val main = Main()

    @Test
    fun testIsExpression() {
        var input = "5 + 3 + 1"
        assertTrue(main.isExpression(input))

    }

    @Test
    fun testValidExpression() {


        println(Pattern.compile("(\\*\\*)|(\\\\)").matcher("2 \\ 2").find())

        assertFalse(main.validExpression("2 ************ 2"))
        assertFalse(main.validExpression("2 // 2"))

        assertFalse(main.validExpression("8 * (2 + 3"))

        assertTrue(main.validExpression("a * 4 / b - (3 - 1)"))

        var input = "123+"
        assertFalse(main.validExpression(input))

        input = "+15"
        assertTrue(main.validExpression(input))

        input = "-22"
        assertTrue(main.validExpression(input))

        input = "22-"
        assertFalse(main.validExpression(input))

        input = "22 33"
        assertFalse(main.validExpression(input))

        input = "b - c + 4 - a"
        assertTrue(main.validExpression(input))

        input = "a -- b - c + 3 --- a ++ 1"
        assertTrue(main.validExpression(input))

        assertTrue(main.validExpression("91 / 13"))




    }

    @Test
    fun isVarAssignment() {
        var input = "n = 3"
        assertTrue(main.validAssignment(input))

        input = "m=4"
        assertTrue(main.validAssignment(input))

        input = "a1 = 8"
        assertFalse(main.validAssignment(input))

        input = "a = 7 = 8"
        assertFalse(main.validAssignment(input))

        input = "a2a"
        assertFalse(main.validAssignment(input))

        input = "var = 2a"
        assertFalse(main.validAssignment(input))


    }

    @Test
    fun assign1() {
        main.assign("n = 5")
        assertEquals(5, main.getVar("n"))

        main.assign("a      = 7")
        assertEquals(7, main.getVar("a"))
    }

    @Test
    fun assign2() {

        main.assign("n = 5")
        main.assign("m=2")
        main.assign("a    =  7")
        assertTrue(main.validAssignment("c=  a"))
        main.assign("c=  a")
        assertEquals(7, main.getVar("c"))
    }

}