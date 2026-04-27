package com.kindfind

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KindFindApp(viewModel: KindFindViewModel = viewModel()) {
    var identifierQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KindFind • Lost & Found Worldwide") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UploadSection(onUpload = viewModel::addPost)

            OutlinedTextField(
                value = identifierQuery,
                onValueChange = { identifierQuery = it },
                label = { Text("Search by ID, plate, wallet detail, kid info...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            val results = viewModel.searchPosts(identifierQuery)
            Text(
                text = "Found records (${results.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                contentPadding = PaddingValues(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { post ->
                    FoundItemCard(post)
                }
            }
        }
    }
}

@Composable
private fun UploadSection(onUpload: (LostFoundPost) -> Unit) {
    var title by remember { mutableStateOf("") }
    var uniqueId by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Upload Found Item", fontWeight = FontWeight.Bold)
            OutlinedTextField(title, { title = it }, label = { Text("Item title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(uniqueId, { uniqueId = it }, label = { Text("Unique identifier (plate/id/details)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(details, { details = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(location, { location = it }, label = { Text("Found location") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(contact, { contact = it }, label = { Text("Contact phone/email") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    if (title.isNotBlank() && uniqueId.isNotBlank() && contact.isNotBlank()) {
                        onUpload(
                            LostFoundPost(
                                title = title.trim(),
                                uniqueIdentifier = uniqueId.trim(),
                                details = details.trim(),
                                location = location.trim(),
                                contact = contact.trim()
                            )
                        )
                        title = ""
                        uniqueId = ""
                        details = ""
                        location = ""
                        contact = ""
                    }
                }
            ) {
                Text("Upload")
            }
        }
    }
}

@Composable
private fun FoundItemCard(post: LostFoundPost) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(post.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Unique ID: ${post.uniqueIdentifier}")
            Text("Details: ${post.details.ifBlank { "Not provided" }}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Location: ${post.location.ifBlank { "Unknown" }}")
            }
            Text("Contact: ${post.contact}", fontWeight = FontWeight.Medium)
        }
    }
}
