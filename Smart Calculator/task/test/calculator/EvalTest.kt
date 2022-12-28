package calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class EvalTest {
    val m = Main()
    val eval = Eval()
    @Test
    fun compute() {
        var input = "5*3+4"
        assertEquals(19, eval.compute(input))

        input = "5*(3+4)"
        assertEquals(35, eval.compute(input))

    }

    @Test
    fun normalizeExpression() {
        var input = "5 * 3 - - 4 + + 1"
        assertEquals("5*3+4+1", eval.normalizeExpression(input))
    }

    @Test
    fun eval() {
        m.assign("a = 9")
        m.assign("b=2")
        m.assign("c = 1")
        val tmp = m.assignValsToVars("a -- b - c + 3 --- a ++ 1")
        assertEquals(eval.compute(tmp), 5)
    }
}