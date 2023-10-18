package com.example.medlemma.View

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medlemma.Model.uploadImageToFirebaseStoragePersonalPhotos
import com.example.medlemma.R
import java.util.UUID
import com.example.medlemma.ui.theme.MedlemmaTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun PersonalInfo(email: String?) {
    MedlemmaTheme {
        val iconState2 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
        var downloadUrl by remember { mutableStateOf<String?>(null) }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val imageName = "your_image_name_${UUID.randomUUID()}.jpg" // Generate a unique name for the image
                if (email != null) {
                    uploadImageToFirebaseStoragePersonalPhotos(email, uri, imageName) { imageUrl ->
                        if (imageUrl != null) {
                            downloadUrl = imageUrl
                            iconState2.value = IconState.COMPLETED
                        } else {

                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val logo: Painter = painterResource(id = R.drawable.medlemmalogo)

                Image(
                    painter = logo,
                    contentDescription = "Medlemma Logo",
                    modifier = Modifier.size(150.dp)
                )

                Button(onClick = {
                    launcher.launch("image/*")
                }) {
                    if (email != null) {
                        Text("User Email: $email")
                    }
                    Spacer(modifier = Modifier.padding(start = 4.dp))
                    when (iconState2.value) {
                        IconState.NOT_COMPLETED -> {
                            CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 0.dp, yOffset = 0.dp)
                        }
                        IconState.COMPLETED -> {
                            CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 0.dp, yOffset = 0.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomIcon(icon: ImageVector, color: Color, xOffset: Dp, yOffset: Dp) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(30.dp) then  Modifier.offset(x = -xOffset, y = yOffset)
    )
}
