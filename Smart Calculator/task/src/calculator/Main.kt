package calculator

import java.math.BigInteger
import java.util.regex.Pattern


fun main() {
    val m = Main()
    while (true) {
        val str = readln().trim()
        //val str = "a    =   7"
        if(str == "") {
            continue
        }

        if (str.startsWith("/")) {
            when (str) {
                "/exit" -> break
                "/help" -> println("some help")
                else -> {
                    println("Unknown command")
                }
            }
            continue
        }
        // var assignment
        if("=" in str) {
            if(m.validAssignment(str)) {
                m.assign(str)
            } else {
                println("Invalid assignment")
            }
        }
        // var query
        else if(m.isQuery(str)) {
            when(val tmp =  m.query(str)) {
                null -> println("Unknown variable")
                else -> println(tmp)
            }
        }
        // mathematical expression
        else if(m.isExpression(str)) {
            if (m.validExpression(str)) {
                val tmp = m.assignValsToVars(str)
                if(tmp.matches(Regex("[^a-zA-Z]*"))) {
                    println(Eval().compute(tmp))
                } else {
                    println("Invalid identifier")
                }
            } else {
                println("Invalid expression")
            }
        }
    }
    println("Bye!")
}

class Main {

    private val vars = mutableMapOf<String, BigInteger>()


    fun assignValsToVars(input : String) : String {
        var tmp = input
        vars.forEach { (k, v) -> tmp = tmp.replace(k, v.toString()) }
        return tmp
    }
    fun validAssignment(input: String): Boolean {
        // ends with digit
        val reg1 = ".*(=\\s*-?\\d+)$".toRegex()
        // ends with valid var
        val reg2 = ".*(=\\s*[a-zA-Z]+)$".toRegex()

        // begins with digits
        val reg3 = "^(\\d+\\s*=).*".toRegex()

        // begins with valid var
        val reg4 = "^([a-zA-Z]+\\s*=).*".toRegex()
        // more than one =
        val reg5 = ".*=.*=.*".toRegex()

        var valid = false
        // ends with
        valid = input.matches(reg1) || input.matches(reg2)

        // begins with
        valid = valid && (input.matches(reg3) || input.matches(reg4))

        valid = valid && !input.matches(reg5)

        return valid
    }

    fun assign(input : String) {
        val l = input.replace(" ", "").split("=")

        // check if right side is another var
        if(l[1].matches(Regex("[a-zA-Z]*"))) {
            // try to find in map
            val tmp = vars[l[1]]
            if (tmp != null) {
                vars[l[0]] = tmp
            } else {
                println("Unknown variable")
            }
        } else {
            vars.put(l[0], l[1].toBigInteger())
        }

    }

    fun isQuery(input: String): Boolean {
        val valid = Pattern.compile("([a-zA-Z]+)")
        return valid.matcher(input.trim()).matches()
    }

    fun query(input : String) : BigInteger? {
        return vars[input]
    }

    fun isExpression(input : String) : Boolean {
        if (input.matches(Regex(".*([\\+\\-\\*/].*)"))) {
            return true
        }
        return false
    }

    fun validExpression(input: String): Boolean {
        if (input.toIntOrNull() != null) {
            return true
        }

        val digitOrVar = "((\\d+)|([a-zA-Z]+))"

        // "1    1" "a   a"
        val reg1 = "$digitOrVar\\s+$digitOrVar".toRegex()

        // "a1a" or "1a1"
        val reg2 = "([a-zA-Z]\\d)|(\\d[a-zA-Z])".toRegex()

        // needs some ops
        val reg4 = ".*[/+=\\-*()]+.*".toRegex()

        if(input.matches(reg1) || input.matches(reg2)) {
            return false
        }

        // it needs to end with a letter, digit or )
        if(!input.last().isLetterOrDigit() && input.last() != ')') {
            return false
        }

        // it needs matching brackets
        if(!validBrackets(input)) {
            return false
        }

        // no ** or \\ or //
        if((Pattern.compile("(\\*\\*)|(\\\\)|(//)").matcher(input)).find()) {
            return false
        }

        // \\ or ** is invalid
        return input.matches(reg4)


    }

    fun validBrackets(input : String) : Boolean {
        var left = 0
        var right = 0

        input.toList().forEach { c ->
            if(c == '(') left++
            else if(c == ')') right++
        }

        return left == right
    }

    fun getVar(key : String) : BigInteger? {
        return vars[key]
    }
}