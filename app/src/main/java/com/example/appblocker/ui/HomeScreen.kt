package com.example.appblocker.ui

//import androidx.core.app.ActivityCompat.startActivityForResult
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appblocker.backend.DayOfTheWeek
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

@Composable
fun HomeScreen(navController: NavHostController, context: Context, modifier: Modifier = Modifier) {
    var devicePolicyManager: DevicePolicyManager? = null
    var deviceAdmin: ComponentName? = null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "App Blocker",
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            //modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(100.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { navController.navigate("ScheduleScreen") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Schedule",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { navController.navigate("BlocklistScreen") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "App Blocklists",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    UsageAccessSettingsPage(context)
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Get Perms",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    showHome(context)
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Go Home",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {

                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .width(350.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    "Manually Start Block",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            isBlocking(context)?.let {
                Button(
                    onClick = {

                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(60.dp)
                        .width(350.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    enabled = it
                ) {
                    Text(
                        "Unlock Block",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Status: ", color = Color.Black, fontSize = 28.sp)

            if (isBlocking(context) == true) {
                Text(text = "Active", color = Color.Green, fontSize = 28.sp)
            } else {
                Text(text = "Inactive", color = Color.Red, fontSize = 28.sp)
            }
        }

        //Button(onClick = {navController.navigate("BlocklistScreen")}) { Text("App Blocklist") }
        //Button(onClick = {}) { Text("Unlock") }
    }
}

fun loadDays(context: Context): List<DayOfTheWeek>? {
    val sharedPreferences = context.getSharedPreferences("days_preferences", Context.MODE_PRIVATE)
    if (sharedPreferences.getString("days", null) == null) {
        return null
    }
    return Json.decodeFromString<List<DayOfTheWeek>>(sharedPreferences.getString("days", null)!!)
}

fun isBlocking(context: Context): Boolean? {
    val calendar = Calendar.getInstance()
    val dayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val days: List<DayOfTheWeek>? = loadDays(context)
    if (loadDays(context) != null) {
        days?.get(dayNum)?.updateIsCurrentlyBlocking()
        return days?.get(dayNum)?.currentlyBlocking
    } else {
        return false
    }
}

fun UsageAccessSettingsPage(context: Context) {
    val intent = Intent()
    intent.action = Settings.ACTION_USAGE_ACCESS_SETTINGS
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}


fun showHome(context: Context): Boolean {
    val startMain = Intent(Intent.ACTION_MAIN)
    startMain.addCategory(Intent.CATEGORY_HOME)
    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(startMain)
    return true
}