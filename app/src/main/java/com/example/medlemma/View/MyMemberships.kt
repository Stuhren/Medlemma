package com.example.medlemma.View

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData

import com.example.medlemma.ViewModel.MyMembershipsViewModel

@Composable
fun MyMemberships(myMembershipsViewModel: MyMembershipsViewModel) {
    val categoriesLiveData: LiveData<List<String>> = myMembershipsViewModel.fetchCategories()
    val categories by categoriesLiveData.observeAsState(initial = emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        GreetingText(message = "MY MEMBERSHIPS", from = categories.joinToString(", "))
    }
}

@Composable
private fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Categories: $from",
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)
        )
    }
}
