package com.roland.android.shrine.ui.screens

import androidx.compose.runtime.Composable
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.ui.layouts.ItemDetail

@Composable
fun DetailScreen(
    data: ItemData
) {
    ItemDetail(item = data)
}