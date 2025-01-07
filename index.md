# Simple Calculator App 🧮📱


### Overview 🌟
This is a Simple Calculator App built using Jetpack Compose for Android. The app allows users to perform basic arithmetic operations, such as addition ➕, subtraction ➖, multiplication ✖️, division ➗, and modulus %, with a real-time result display for every valid operation. The app also includes features to ensure correct operator usage and prevent invalid expressions. 🛡️

### Features ✨
Real-Time Results ⚡:

The app updates the result after every valid operation.
No need to press = for intermediate results.
Input Validation ✅:

Operators cannot be used at the start of the expression. 🚫
Only one operator is allowed at a time (e.g., no ++, --, **, etc.).
Basic Arithmetic Operations 🧩:

Supports addition (+), subtraction (-), multiplication (*), division (/), and modulus (%).

---

### User-Friendly Interface 🖥️:

Clear button (C) to reset the expression. 🔄
Delete button (Del) to remove the last character. 🗑️
Toast messages 🍞 for invalid actions.
Expression Screen 🖋️
Buttons Layout 🎛️

### Requirements 🛠️

Android Studio Arctic Fox 🦊 or later
Kotlin 1.5+ 📜
Minimum SDK: 21 (Android 5.0) 📱
Jetpack Compose: 1.0.0 or later 🏗️

---

### How It Works 🧠
Core Functionalities 🔍
Tokenization:
The input expression is tokenized into numbers and operators using the tokenizeExpression() function. ✂️

Postfix Conversion:
The convertToPostfix() function converts the infix expression to postfix using a stack-based approach to handle operator precedence. 📤

Evaluation:
The evaluatePostfix() function evaluates the postfix expression to calculate the result. 🧮

### UI Interaction:

Buttons dynamically update the expression. 🎛️
The result updates instantly after every valid input. 🕒

### Installation 💻
Clone the repository:

git clone
 
```https://github.com/Sidd-Tiwari/simple-calculator.git```

Open the project in Android Studio. 🛠️
Sync the project with Gradle files. 🔄
Run the app on an emulator or physical device. 🚀

### Usage Instructions 📋

Launch the app. 🚀
Use the buttons to enter numbers and operations. 🔢 
The result will update dynamically after each valid input. ✨
Use the C button to clear the expression and result. 🔄
Use the Del button to delete the last character. 🗑️

### Code Highlights 💡
Real-Time Result Calculation ⚡

```
if (expression.last() !in "+-*/%") {
    try {
        val postfix = convertToPostfix(expression)
        val evalResult = evaluatePostfix(postfix)
        result = evalResult.toString()
    } catch (e: Exception) {
        result = "Error"
    }
}
```
---
### License 📜
This project is licensed under the MIT License. Feel free to use and modify it! 😄

### Feedback & Contribution 🤝
We ❤️ contributions! If you find any issues or want to add new features, feel free to open a pull request or raise an issue. Let's build together! 💪

#### Enjoy calculating! 🎉
