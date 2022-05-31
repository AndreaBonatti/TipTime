package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    val focusManager = LocalFocusManager.current
    // The hoisted params from the EditNumberField Compose Function
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount, tipPercent, roundUp)

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
        // Bill Amount
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            value = amountInput,
            onValueChange = { amountInput = it }
        )
        // Tip Percentage
        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
            value = tipInput,
            onValueChange = { tipInput = it }
        )
        RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it })
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
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
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
        label = { Text(stringResource(id = label)) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        keyboardOptions = KeyboardOptions.Default.copy(
//            keyboardType = KeyboardType.Number,
//            imeAction = ImeAction.Next
//        )
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray
            )
        )
    }
}

@VisibleForTesting /* private -> internal */
internal fun calculateTip(
    amount: Double,
    tipPercent: Double = 15.0,
    roundUp: Boolean
): String {
    var tip = tipPercent / 100 * amount
    if (roundUp)
        tip = kotlin.math.ceil(tip)
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeScreen()
    }
}