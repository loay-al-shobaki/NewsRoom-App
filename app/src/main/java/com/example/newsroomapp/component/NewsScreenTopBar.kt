package com.example.newsroomapp.component


import android.content.res.Resources.Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchIconClicked: () -> Unit
) {
    TopAppBar(
        scrollBehavior =scrollBehavior,
        title = { Text(text = "Newsroom", fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { onSearchIconClicked.invoke() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Serch")
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor =MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )

}