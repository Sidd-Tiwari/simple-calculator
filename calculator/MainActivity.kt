package com.example.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import java.util.Stack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    val context = LocalContext.current
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    fun updateExpression(input: String) {
        if (input in "+-*/%") {
            // Avoid adding an operator if there's no preceding number
            if (expression.isEmpty() || expression.last() in "+-*/%") {
                Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show()
                return
            }
        }

        expression += input

        if (expression.last() !in "+-*/%") {
            try {
                val postfix = convertToPostfix(expression)
                val evalResult = evaluatePostfix(postfix)
                result = evalResult.toString()
            } catch (e: Exception) {
                result = "Error"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the current expression and result
        Text(
            text = expression.ifEmpty { "0" },
            fontSize = 36.sp,
            color = Color.Black,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = result,
            fontSize = 24.sp,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f)) // Push buttons to the bottom of the screen

        // Calculator buttons
        val buttons = listOf(
            listOf("C", "Del", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf(".", "0", "=")
        )

        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (label in row) {
                    Button(
                        onClick = {
                            when (label) {
                                "C" -> {
                                    expression = ""
                                    result = ""
                                    Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
                                }
                                "Del" -> {
                                    if (expression.isNotEmpty()) {
                                        expression = expression.dropLast(1)
                                        if (expression.lastOrNull()!! !in "+-*/%") {
                                            try {
                                                val postfix = convertToPostfix(expression)
                                                val evalResult = evaluatePostfix(postfix)
                                                result = evalResult.toString()
                                            } catch (e: Exception) {
                                                result = ""
                                            }
                                        } else {
                                            result = ""
                                        }
                                    }
                                }
                                "=" -> {
                                    try {
                                        val postfix = convertToPostfix(expression)
                                        val evalResult = evaluatePostfix(postfix)
                                        result = evalResult.toString()
                                    } catch (e: Exception) {
                                        result = "Error"
                                    }
                                }
                                else -> updateExpression(label)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (label == "C") Color.Red else Color(0xFF03A9F4)
                        )
                    ) {
                        Text(text = label, fontSize = 24.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// Tokenize the input expression
fun tokenizeExpression(expression: String): List<String> {
    val tokens = mutableListOf<String>()
    var numberBuffer = StringBuilder()

    for (char in expression) {
        if (char.isDigit() || char == '.') {
            numberBuffer.append(char)
        } else {
            if (numberBuffer.isNotEmpty()) {
                tokens.add(numberBuffer.toString())
                numberBuffer = StringBuilder()
            }
            if (char.toString() in "+-*/%") {
                tokens.add(char.toString())
            }
        }
    }

    if (numberBuffer.isNotEmpty()) {
        tokens.add(numberBuffer.toString())
    }

    return tokens
}

// Convert infix to postfix notation
fun convertToPostfix(expression: String): List<String> {
    val precedence = mapOf(
        "+" to 1,
        "-" to 1,
        "*" to 2,
        "/" to 2,
        "%" to 2
    )
    val operators = Stack<String>()
    val postfix = mutableListOf<String>()
    val tokens = tokenizeExpression(expression)

    for (token in tokens) {
        when {
            token.toDoubleOrNull() != null -> postfix.add(token) // Numbers go directly to postfix
            token in precedence.keys -> {
                while (operators.isNotEmpty() && precedence[operators.peek()] ?: 0 >= precedence[token] ?: 0) {
                    postfix.add(operators.pop())
                }
                operators.push(token)
            }
            else -> throw IllegalArgumentException("Invalid token: $token") // Ensures proper validation
        }
    }

    while (operators.isNotEmpty()) {
        postfix.add(operators.pop())
    }
    return postfix
}

// Evaluate the postfix expression
fun evaluatePostfix(postfix: List<String>): Double {
    val stack = Stack<Double>()
    for (token in postfix) {
        when {
            token.toDoubleOrNull() != null -> stack.push(token.toDouble())
            else -> {
                if (stack.size < 2) throw IllegalArgumentException("Invalid expression")
                val b = stack.pop()
                val a = stack.pop()
                stack.push(
                    when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> if (b != 0.0) a / b else throw ArithmeticException("Divide by zero")
                        "%" -> a % b
                        else -> throw IllegalArgumentException("Invalid operator: $token")
                    }
                )
            }
        }
    }
    if (stack.size != 1) throw IllegalArgumentException("Invalid postfix evaluation")
    return stack.pop()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}
