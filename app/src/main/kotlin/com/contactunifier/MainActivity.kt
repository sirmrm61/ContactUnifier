package com.contactunifier

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.contactunifier.presentation.navigation.NavGraph
import com.contactunifier.presentation.permissions.PermissionRationaleScreen
import com.contactunifier.presentation.permissions.getRequiredPermissions
import com.contactunifier.presentation.ui.ContactUnifierTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity entry point for the Contact Unifier app.
 * Sets up Compose UI, navigation, and handles runtime permissions.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactUnifierTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var permissionsGranted by remember { mutableStateOf(false) }
                    var showPermissionRationale by remember { mutableStateOf(false) }

                    // Check if permissions are already granted
                    val allPermissionsGranted = remember {
                        getRequiredPermissions().all {
                            checkSelfPermission(it) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        }
                    }

                    // Permission launcher
                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        permissionsGranted = permissions.values.all { it }
                        if (permissionsGranted) {
                            showPermissionRationale = false
                        }
                    }

                    // Show appropriate screen
                    when {
                        allPermissionsGranted || permissionsGranted -> {
                            NavGraph()
                        }
                        showPermissionRationale -> {
                            PermissionRationaleScreen(
                                onRequestPermissions = {
                                    permissionLauncher.launch(getRequiredPermissions())
                                },
                                onDismiss = {
                                    // User declined - still show nav graph but contacts may not load
                                    NavGraph()
                                }
                            )
                        }
                        else -> {
                            // First time - show rationale
                            showPermissionRationale = true
                        }
                    }
                }
            }
        }
    }
}
