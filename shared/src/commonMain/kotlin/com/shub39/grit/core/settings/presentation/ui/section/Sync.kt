package com.shub39.grit.core.settings.presentation.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.shub39.grit.core.settings.presentation.SettingsAction
import com.shub39.grit.core.settings.presentation.SettingsState
import com.shub39.grit.core.shared_ui.detachedItemShape
import com.shub39.grit.core.shared_ui.endItemShape
import com.shub39.grit.core.shared_ui.leadingItemShape
import com.shub39.grit.core.shared_ui.listItemColors
import com.shub39.grit.core.theme.flexFontEmphasis
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.backspace
import grit.shared.generated.resources.download
import grit.shared.generated.resources.import_all
import grit.shared.generated.resources.import_all_descr
import grit.shared.generated.resources.nav_arrow_back
import grit.shared.generated.resources.play_arrow
import grit.shared.generated.resources.queue
import grit.shared.generated.resources.queue_size
import grit.shared.generated.resources.sync
import grit.shared.generated.resources.sync_server_url
import grit.shared.generated.resources.sync_server_url_none
import grit.shared.generated.resources.tray_full
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SyncPage(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var syncServerDialog by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier.fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .background(MaterialTheme.colorScheme.background)
    ) {
        MediumFlexibleTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(text = stringResource(Res.string.sync), fontFamily = flexFontEmphasis())
            },
            navigationIcon = {
                FilledTonalIconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.nav_arrow_back),
                        contentDescription = "Navigate Back",
                    )
                }
            },
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 60.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Column(modifier = Modifier.clip(detachedItemShape())) {
                        ListItem(
                            headlineContent = { Text(text = stringResource(Res.string.sync_server_url)) },
                            leadingContent = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.sync),
                                    contentDescription = null,
                                )
                            },
                            colors = listItemColors(),
                            supportingContent = {
                                Text(
                                    text = state.syncState.url
                                        ?: stringResource(Res.string.sync_server_url_none)
                                )
                            },
                            modifier = Modifier
                                .clickable { syncServerDialog = true },
                        )
                    }

                    if (state.syncState.url == null) return@Column

                    Spacer(modifier = Modifier.height(4.dp))

                    Column(modifier = Modifier.clip(leadingItemShape())) {
                        ListItem(
                            colors = listItemColors(),
                            leadingContent = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.download),
                                    contentDescription = null,
                                )
                            },
                            headlineContent = { Text(text = stringResource(Res.string.import_all)) },
                            supportingContent = {
                                Text(text = stringResource(Res.string.import_all_descr))
                            },
                        )

                        Row(
                            modifier =
                                Modifier.fillParentMaxWidth()
                                    .background(listItemColors().containerColor)
                                    .padding(start = 52.dp, end = 16.dp, bottom = 8.dp)
                        ) {
                            Button(
                                onClick = { onAction(SettingsAction.ImportAllFromServer) },
                                enabled = !(state.syncState.busyImporting || state.syncState.busyElaborating),
                                modifier = Modifier.weight(1f),
                            ) {
                                if (state.syncState.busyImporting) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                } else {
                                    Icon(
                                        painter = painterResource(Res.drawable.play_arrow),
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.clip(endItemShape())) {
                        ListItem(
                            headlineContent = { Text(text = stringResource(Res.string.queue)) },
                            leadingContent = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.tray_full),
                                    contentDescription = null,
                                )
                            },
                            colors = listItemColors(),
                            supportingContent = {
                                Text(
                                    text = stringResource(
                                        Res.string.queue_size,
                                        state.syncState.queueSize
                                    )
                                )
                            },
                        )

                        if (state.syncState.queueSize > 0) {
                            Row(
                                modifier =
                                    Modifier.fillParentMaxWidth()
                                        .background(listItemColors().containerColor)
                                        .padding(start = 52.dp, end = 16.dp, bottom = 8.dp)
                            ) {
                                Button(
                                    onClick = { onAction(SettingsAction.ElaborateQueue) },
                                    enabled = !(state.syncState.busyImporting || state.syncState.busyElaborating),
                                    modifier = Modifier.weight(1f),
                                ) {
                                    if (state.syncState.busyElaborating) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    } else {
                                        Icon(
                                            painter = painterResource(Res.drawable.sync),
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (syncServerDialog) {
        SyncServerUrlDialog(
            state,
            onAction,
            onDismissRequest = { syncServerDialog = false },
        )
    }
}

@Composable
fun SyncServerUrlDialog(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var inputText by remember { mutableStateOf(state.syncState.url.orEmpty()) }
    AlertDialog(
        onDismissRequest,
        title = { Text("Enter sync server URL") },
        text = {
            Column {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("URL") },
                    singleLine = false,
                    trailingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.backspace),
                            contentDescription = "clear text",
                            modifier = Modifier
                                .clickable {
                                    inputText = ""
                                }
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAction(SettingsAction.ChangeSyncServerUrl(inputText))
                onDismissRequest()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )

}