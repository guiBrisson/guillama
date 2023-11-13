package presentation.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.ModelLibrary
import presentation.designsystem.component.MainButton
import presentation.screen.main.DownloadModelUiState
import presentation.screen.main.ModelListUiState
import presentation.utils.loadSvgPainter

object SelectedModel {
    var value by mutableStateOf<ModelLibrary?>(null)
}

@Composable
fun ModelSelect(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    modelListUiState: ModelListUiState,
    downloadModelUiState: DownloadModelUiState,
    isServerRunning: Boolean,
    onDownloadModel: (model: ModelLibrary) -> Unit,
    onRemoveModel: (model: ModelLibrary) -> Unit,
) {
    var showModelListDialog by remember { mutableStateOf(false) }
    val closeModelListDialog = { showModelListDialog = false }

    var showDownloadingDialog by remember { mutableStateOf(false) }
    val closeDownloadingDialog = { showDownloadingDialog = false }

    var showServerDisconnectedDialog by remember { mutableStateOf(false) }
    val closeServerDisconnectedDialog = { showServerDisconnectedDialog = false }

    var selectModelText by remember { mutableStateOf("Select a model") }
    val ableToOpenModelList = modelListUiState.isSuccess() && !downloadModelUiState.isDownloading()

    val arrowRotation: Float by animateFloatAsState(if (showModelListDialog || showDownloadingDialog) 0f else 180f)

    LaunchedEffect(SelectedModel.value) {
        selectModelText = if (SelectedModel.value?.modelName.isNullOrEmpty()) {
            "Select a model"
        } else {
            SelectedModel.value!!.modelName
        }
    }

    MainButton(
        modifier = Modifier.clip(RoundedCornerShape(4.dp)),
        onClick = {
            if (!isServerRunning) showServerDisconnectedDialog = true
            else if (ableToOpenModelList) showModelListDialog = true
            else showDownloadingDialog = true
        },
    ) {
        Row(
            modifier = modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedVisibility(isExpanded) {
                Text(text = selectModelText, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }

            if (!isServerRunning) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = loadSvgPainter("icons/ic_warning.svg"),
                    contentDescription = null,
                )
            } else if (downloadModelUiState.isDownloading()) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
            } else if (modelListUiState.isLoading()) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round,
                )
            } else {
                if (isExpanded) {
                    Icon(
                        modifier = Modifier.size(16.dp).rotate(arrowRotation),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = loadSvgPainter("icons/ic_ai_model.svg"),
                        contentDescription = null,
                    )
                }
            }
        }

        if (!isServerRunning) {
            DisconnectedServerDialog(
                showDialog = showServerDisconnectedDialog,
                onCloseDialog = closeServerDisconnectedDialog,
            )
        } else if (ableToOpenModelList) {
            ModelSelectDialog(
                showDialog = showModelListDialog,
                onCloseDialog = closeModelListDialog,
                modelListUiState = modelListUiState as ModelListUiState.Success,
                onDownloadModel = onDownloadModel,
                onRemoveModel = onRemoveModel,
            )
        } else {
            SelectedModel.value?.let { model ->
                DownloadingModelDialog(
                    showDialog = showDownloadingDialog,
                    onCloseDialog = closeDownloadingDialog,
                    model = model,
                )
            }
        }


    }
}

@Composable
private fun ModelSelectDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onCloseDialog: () -> Unit,
    modelListUiState: ModelListUiState.Success,
    onDownloadModel: (model: ModelLibrary) -> Unit,
    onRemoveModel: (model: ModelLibrary) -> Unit
) {
    DropdownMenu(
        modifier = modifier.background(MaterialTheme.colors.surface),
        expanded = showDialog,
        onDismissRequest = { onCloseDialog() },
    ) {
        for (model in enumValues<ModelLibrary>()) {
            val m = modelListUiState.models.map { it.name }.find { it.contains(model.modelName, true) }
            val isDownloaded: Boolean = m != null

            Row(Modifier.padding(vertical = 2.dp, horizontal = 8.dp).height(28.dp)) {
                DropdownMenuItem(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onCloseDialog()
                        if (!isDownloaded) onDownloadModel(model)
                        SelectedModel.value = model
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (!isDownloaded) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "${model.modelName} (${model.size}GB)",
                            fontSize = 14.sp,
                        )
                    }
                }

                Box(
                    modifier = Modifier.size(28.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isDownloaded) {
                        MainButton(
                            modifier = Modifier.size(28.dp),
                            onClick = {
                                onRemoveModel(model)
                                if (SelectedModel.value == model) SelectedModel.value = null
                            },
                        ) {
                            Icon(
                                modifier = Modifier.padding(2.dp).size(16.dp),
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DownloadingModelDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onCloseDialog: () -> Unit,
    model: ModelLibrary,
) {
    DropdownMenu(
        modifier = modifier.background(MaterialTheme.colors.surface).padding(horizontal = 12.dp),
        expanded = showDialog,
        onDismissRequest = { onCloseDialog() },
    ) {
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LinearProgressIndicator(modifier = Modifier.width(200.dp))
            Text("${model.size}gb total", fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun DisconnectedServerDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onCloseDialog: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier.background(MaterialTheme.colors.surface).padding(horizontal = 12.dp),
        expanded = showDialog,
        onDismissRequest = { onCloseDialog() },
    ) {
        Text(text = "Server is not running", fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}
