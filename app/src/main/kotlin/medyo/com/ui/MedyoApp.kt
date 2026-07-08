/*
 * Copyright 2026 The Medyo Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package medyo.com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import medyo.com.core.design_system.component.navigation.CustomBottomNavigation
import medyo.com.core.navigation.Navigator
import medyo.com.core.navigation.toEntries
import medyo.com.bio_scan.api.BioScanNavKey
import medyo.com.bio_scan.impl.navigation.bioScanEntry
import medyo.com.feature.detail.impl.navigation.MedicationDetailEntry
import medyo.com.feature.home.api.HomeNavKey
import medyo.com.feature.home.impl.navigation.homeEntry
import medyo.com.feature.scanner.impl.navigation.scannerEntry
import medyo.com.settings.api.SettingsNavKey
import medyo.com.settings.impl.navigation.settingsEntry
import medyo.com.expiry_dashboard.impl.navigation.expiryDashboardEntry

import android.Manifest
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MedyoApp(
    appState: MedyoAppState = rememberMedyoAppState(),
    modifier: Modifier = Modifier
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationsPermissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )
        LaunchedEffect(Unit) {
            if (!notificationsPermissionState.status.isGranted) {
                notificationsPermissionState.launchPermissionRequest()
            }
        }
    }

    val navigator = remember(appState.navigationState) { Navigator(appState.navigationState) }

    // 1. RESOLVE SELECTED TAB: Home = 0, Settings = 1, BioScan = -1
    val selectedTab = when (appState.navigationState.currentTopLevelKey) {
        HomeNavKey -> 0
        SettingsNavKey -> 1
        else -> -1
    }

    // 2. VISIBILITY LOGIC: Show bottom bar ONLY when current active screen is a top-level tab root
    val showBottomBar = appState.navigationState.currentKey in appState.navigationState.topLevelKeys

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // 3. ANIMATED TRANSITION: Smoothly slide the bottom bar in/out
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ) + fadeOut()
            ) {
                CustomBottomNavigation(
                    modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
                    selectedTab = selectedTab,
                    onTabSelected = { tabIndex ->
                        val targetKey = when (tabIndex) {
                            0 -> HomeNavKey
                            1 -> SettingsNavKey
                            else -> BioScanNavKey
                        }
                        navigator.navigate(targetKey)
                    },
                    onCentralButtonClick = {
                        navigator.navigate(BioScanNavKey)
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // 4. REACTIVE HEIGHT ADAPTATION: The Scaffold padding dynamically updates to 0.dp
                //    when bottomBar is hidden, smoothly expanding content to fill the screen!
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            val entryProvider = entryProvider {
                homeEntry(navigator)
                bioScanEntry(navigator)
                scannerEntry(navigator)
                settingsEntry(navigator)
                MedicationDetailEntry(navigator)
                expiryDashboardEntry(navigator)
            }

            NavDisplay(
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}
