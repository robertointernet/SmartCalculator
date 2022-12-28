package calculator

import java.math.BigInteger
import java.util.*


class Eval {

    private val vals = ArrayDeque<BigInteger>()
    private val ops = ArrayDeque<Char>()

    fun normalizeExpression(input : String) : String {
        val tmp = input.replace(" ", "")

        val stack = ArrayDeque<Char>()
        val result = ArrayDeque<Char>()

        stack.addAll(tmp.toList().reversed())

        //var normalized = ""

        while(!stack.isEmpty()) {
            if(stack.peek().isDigit()) {
                result.push(stack.pop())
            } else if (result.peek() in listOf('+', '-') && stack.peek() in listOf('+', '-')) {
                if(result.peek() != stack.peek()) {
                    // +- or -+
                    result.pop()
                    result.push('-')
                    stack.pop()
                } else if (stack.peek() == '-') {
                    // --
                    result.pop()
                    result.push('+')
                    stack.pop()
                } else {
                    // ++
                    stack.pop()
                }

            } else {
                result.push(stack.pop())
            }
        }
        return String(result.toCharArray())
    }


    fun compute(input: String): BigInteger {
        var prevIsDigit = false

        val tmp = normalizeExpression(input)

        for (ch in tmp) {

            if (!ch.isDigit()) {
                prevIsDigit = false
            }

            if (ch.isDigit()) {
                if (prevIsDigit) {
                    // append the digit to the previous one
                    val num = vals.pop()
                    val tmp = num.toString() + ch
                    vals.push(tmp.toBigInteger())

                } else {
                    vals.push(BigInteger(ch.toString()))
                }
                prevIsDigit = true
            } else if (ch == '(') {
                ops.push(ch)
            }
            // Closing brace encountered,
            // solve entire brace
            else if (ch == ')') {
                while (ops.peek() != '(') {
                    vals.push(applyOp(ops.pop(), vals.pop(), vals.pop()))
                }
                ops.pop();
            }

            // Current token is an operator.

            else if (ch in listOf('+', '-', '*', '/')) {
                // While top of 'ops' has same
                // or greater precedence to current
                // token, which is an operator.
                // Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.isEmpty() && hasPrecedence(ch, ops.peek())) {
                    vals.push(applyOp(ops.pop(), vals.pop(), vals.pop()));
                    // Push current token to 'ops'.
                }
                ops.push(ch);

            }
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.isEmpty()) {
            vals.push(applyOp(ops.pop(), vals.pop(), vals.pop()))
        }

        // Top of 'values' contains
        // result, return it
        return vals.pop();

    }


    // A utility method to apply an
    // operator 'op' on operands 'a'
    // and 'b'. Return the result.
    private fun applyOp(op: Char, b: BigInteger, a: BigInteger): BigInteger {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '*' -> return a * b
            '/' -> {
                if (b == BigInteger("0")) throw UnsupportedOperationException(
                    "Cannot divide by zero"
                )
                return a / b
            }
        }
        return BigInteger("0")
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') {
            return false
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false
        }
        return true
    }

}

