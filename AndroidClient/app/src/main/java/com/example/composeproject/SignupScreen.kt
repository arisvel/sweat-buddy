package com.example.composeproject

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeproject.ui.theme.AquaLogo
import com.example.composeproject.ui.theme.Blue1
import com.example.composeproject.ui.theme.Blue2
import com.example.composeproject.ui.theme.Blue4
import com.example.composeproject.ui.theme.Blue5
import com.example.composeproject.ui.theme.ManropeFamily
import com.example.composeproject.ui.theme.WhiteBlue1


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun Signup() {
    Box(modifier = Modifier
        .offset(x = (-120).dp, y = (-250).dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(450.dp),
            onDraw = {
                drawCircle(
                    color = Blue1
                )
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.clip(androidx.compose.foundation.shape.CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            Text(
                text = "Back", color = Color.White,
                fontFamily = ManropeFamily,
                fontSize = 23.sp)
        }
        Text(
            text = "Sign Up", color = WhiteBlue1,
            fontFamily = ManropeFamily,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 30.dp)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Blue2,
                            1f to Blue1,
                        ),
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 390.dp)
            ) {
                val canvasWidth = size.width*2
                val arcHeight = 3600f
                drawArc(
                    color = Blue2.copy(alpha = 0.05f),
                    startAngle = 0f,
                    sweepAngle = -180f,
                    useCenter = false,
                    topLeft = Offset(-size.width/2, -arcHeight/2),
                    size = Size(canvasWidth, arcHeight)
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 470.dp)
            ) {
                val canvasWidth = size.width*2
                val arcHeight = 3600f
                drawArc(
                    color = Blue2.copy(alpha = 0.1f),
                    startAngle = 0f,
                    sweepAngle = -180f,
                    useCenter = false,
                    topLeft = Offset(-size.width/2, -arcHeight/2),
                    size = Size(canvasWidth, arcHeight)
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 550.dp),
            ) {
                val canvasWidth = size.width*2
                val arcHeight = 3600f
                drawArc(
                    color = Blue2,
                    startAngle = 0f,
                    sweepAngle = -180f,
                    useCenter = false,
                    topLeft = Offset(-size.width/2, -arcHeight/2),
                    size = Size(canvasWidth, arcHeight)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text( text = "Username", color = Color.White,
                    fontFamily = ManropeFamily,
                    fontSize = 15.sp
                )
                LoginTextField("Type here...")
                Spacer(modifier = Modifier.height(30.dp))
                Text( text = "Password", color = Color.White,
                    fontFamily = ManropeFamily,
                    fontSize = 15.sp
                )
                LoginTextField("Type here...")
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {/*TODO*/},
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhiteBlue1),
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(55.dp),
                ) {
                    Text(
                        text = "Sign Up", color = Blue1, fontFamily = ManropeFamily,
                        fontSize = 19.sp, fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupTextField(title: String) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = text,
        label = { Text(text = title, color = WhiteBlue1) },
        onValueChange = {
            text = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = WhiteBlue1,
            textColor = WhiteBlue1,
            containerColor = Blue5
        ),
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(60.dp)
    )
}