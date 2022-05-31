package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }
}

@Composable
fun TipTimeScreen() {
    // The hoisted params from the EditNumberField Compose Function
    var amountInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount)

    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.calculate_tip),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        EditNumberField(
            value = amountInput,
            onValueChange = {amountInput = it}
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
//            text = stringResource(id = R.string.tip_amount, ""),
            text = stringResource(id = R.string.tip_amount, tip),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Note: During initial composition, value in the TextField is set to the initial value, which is an empty string.
 * When the user enters text into the text field, the onValueChange lambda callback is called, the lambda executes, and the amountInput.value is set to the updated value entered in the text field.
 * The amountInput is the mutable state being tracked by the Compose, recomposition is scheduled. The EditNumberField() composable function is recomposed. Since you are using remember { } the change survives the recomposition, that is the state is not re-initialized to "".
 * The value of the text field is set to the remembered value of amountInput. The text field recomposes (redrawn on the screen with new value).
 */
@Composable
fun EditNumberField(
    // Params to do the hoisting of tipAmount
    value: String,
    onValueChange: (String) -> Unit
) {
//    val amountInput = "0"
//    var amountInput: MutableState<String> = mutableStateOf("0") /* to make the value observable */
//    var amountInput = mutableStateOf("0") /* to make the value observable, re-initalization of the value on every re-composition */
//    var amountInput by remember { mutableStateOf("") } /*  remember function store an object across recompositions */
//
//    /* The toDoubleOrNull() function is a predefined Kotlin function that parses a string as a Double number and returns the result or null if the string isn't a valid representation of a number. */
//    val amount = amountInput.toDoubleOrNull()
//        ?: 0.0 /* Elvis operator that returns a 0.0 value when amountInput is null */
//    val tip = calculateTip(amount)

    TextField(
//        value = amountInput,
//        value = amountInput.value,
//        onValueChange = { amountInput.value = it }
//        value = amountInput,
//        onValueChange = { amountInput = it },
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.cost_of_service)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

private fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0
): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}