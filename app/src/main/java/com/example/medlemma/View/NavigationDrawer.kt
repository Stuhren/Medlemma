package com.example.medlemma.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medlemma.ViewModel.MenuItem
import kotlinx.coroutines.launch
import com.example.medlemma.ui.theme.SoftGray
import com.example.medlemma.ui.theme.CustomShapes
import com.example.medlemma.ui.theme.MedlemmaTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import com.example.medlemma.R

@Composable
fun DrawerHeader() {
    MedlemmaTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoftGray)  // Set the background color
                .padding(vertical = 44.dp),
            contentAlignment = Alignment.Center
        ) {
            val logo: Painter = painterResource(id = R.drawable.medlemmalogo)
            Image(
                painter = logo,
                contentDescription = "Medlemma Logo",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit,
    scaffoldState: ScaffoldState
) {
    MedlemmaTheme {
        Box(modifier = Modifier.fillMaxHeight().background(SoftGray)) {  // Fill the entire height and set the background color
            val scope = rememberCoroutineScope()
            LazyColumn(modifier) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onItemClick(item)
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.title,
                            style = itemTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}