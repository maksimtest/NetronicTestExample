package com.netronic.test.presentation.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.netronic.test.domain.model.User
import com.netronic.test.logging.AppLogger
import com.netronic.test.presentation.common.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    onUserClick: (String) -> Unit,
    vm: UsersViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val tag = "UI.Users"

    Scaffold(topBar = { TopAppBar(title = { Text("Users") }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            AppLogger.d(tag, "render | state=${state::class.simpleName}")
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> Text("Error: ${'$'}{s.message}", Modifier.align(Alignment.Center))
                is UiState.Success -> UsersList(s.data, onUserClick)
            }
        }
    }
}


@Composable
private fun UsersList(users: List<User>, onUserClick: (String) -> Unit) {
    val tag = "UI.Users"
    LazyColumn(Modifier.fillMaxSize()) {
        items(users, key = { it.id }) { user ->
            UserRow(user) {
                AppLogger.d(tag, "click | userId=${user.id} name=${user.fullName}")
                onUserClick(user.id)
            }
        }
    }
}


@Composable
private fun UserRow(user: User, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.pictureUrl,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(user.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(user.email, style = MaterialTheme.typography.bodyMedium)
        }
    }
}