package com.example.medlemma.View

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.medlemma.Model.removeIdentification
import com.example.medlemma.Model.uploadImageToFirebaseStoragePersonalPhotos
import com.example.medlemma.R
import java.util.UUID
import com.example.medlemma.ui.theme.MedlemmaTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.medlemma.ui.theme.DarkGray
import com.example.medlemma.ui.theme.blue2
import java.time.format.TextStyle
import com.example.medlemma.ViewModel.MyMembershipsViewModel
import com.example.medlemma.ui.theme.CustomShapes
import com.example.medlemma.ui.theme.RedTest

@Composable
fun PersonalInfo(email: String?) {
    MedlemmaTheme {
        val viewModel: MyMembershipsViewModel = viewModel()

        val iconState2 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
        val iconState3 = remember { mutableStateOf(IconState.NOT_COMPLETED) }
        var downloadUrl by remember { mutableStateOf<String?>(null) }
        val members by viewModel.fetchAllMembers().observeAsState(initial = emptyList())
        val currentMember = members.find { it.email == email }
        //var imagePainter = rememberImagePainter(data = currentMember?.identificationURL)

        var selectedImageUri by remember { mutableStateOf("") }

        if (currentMember != null) {
            selectedImageUri = currentMember.identificationURL
        }
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri.toString()
            if (uri != null) {
                val imageName = "your_image_name_${UUID.randomUUID()}.jpg" // Generate a unique name for the image
                if (email != null) {
                    uploadImageToFirebaseStoragePersonalPhotos(email, uri, imageName) { imageUrl ->
                        if (imageUrl != null) {
                            downloadUrl = imageUrl
                            iconState2.value = IconState.COMPLETED

                        } else {
                            // Handle upload failure and show an error message
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val logo: Painter = painterResource(id = R.drawable.medlemmalogo)

                Image(
                    painter = logo,
                    contentDescription = "Medlemma Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 50.dp)
                )
                Spacer(modifier = Modifier.padding(20.dp))

                if (currentMember != null) {
                    Card(
                        modifier = Modifier
                            .width(200.dp) // Adjust the width as needed
                            .height(200.dp) // Adjust the height as needed
                            .border(
                                4.dp,
                                DarkGray,
                                shape = CustomShapes.medium
                            )
                    ) {
                        Image(
                            painter = rememberImagePainter(data = selectedImageUri),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "🚗 Upload Driver's License QR Code 📸",
                    color = DarkGray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // Use a Button with clear instructions
                Button(onClick = {
                    launcher.launch("image/*")
                    selectedImageUri?.let { uri ->
                        println("Selected URI: $uri")
                    }
                }) {
                    if (email != null) {
                        Text("Select your QR code")
                    }

                    when (iconState2.value) {
                        IconState.NOT_COMPLETED -> {
                            CustomIcon(Icons.Outlined.Clear, Color.Red, xOffset = 0.dp, yOffset = 0.dp)
                        }
                        IconState.COMPLETED -> {
                            CustomIcon(Icons.Outlined.CheckCircle, Color.Green, xOffset = 0.dp, yOffset = 0.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))


                Text("Delete your QR code 🗑️", color = DarkGray)

                val deleteText = "Delete"
                val deleteString = AnnotatedString.Builder().apply {
                    withStyle(
                        style = SpanStyle(
                            color = RedTest // You can change the color as needed
                        )
                    ) {
                        append(deleteText)
                    }
                }.toAnnotatedString()

                ClickableText(
                    text = deleteString,
                    style = MaterialTheme.typography.bodyLarge,
                    onClick = {
                        // Handle the delete action here
                        if (email != null) {
                            removeIdentification(email) { removed ->
                                if (removed) {
                                    // Handle successful deletion
                                    iconState3.value = IconState.COMPLETED
                                    selectedImageUri = ""
                                } else {
                                    // Handle the failure, e.g., show an error message
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
