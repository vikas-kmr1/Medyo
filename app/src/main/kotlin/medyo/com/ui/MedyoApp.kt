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
import medyo.com.feature.home.api.HomeNavKey
import medyo.com.feature.home.impl.navigation.homeEntry
import medyo.com.feature.scanner.impl.navigation.scannerEntry
import medyo.com.settings.api.SettingsNavKey
import medyo.com.settings.impl.navigation.settingsEntry

@Composable
fun MedyoApp(
    appState: MedyoAppState = rememberMedyoAppState(),
    modifier: Modifier = Modifier
) {
    val navigator = remember(appState.navigationState) { Navigator(appState.navigationState) }

    val selectedTab = when (appState.navigationState.currentTopLevelKey) {
        HomeNavKey -> 0
        SettingsNavKey -> 1
        else -> -1
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            val entryProvider = entryProvider {
                homeEntry(navigator)
                bioScanEntry(navigator)
                scannerEntry(navigator)
                settingsEntry(navigator)
            }

            NavDisplay(
                entries = appState.navigationState.toEntries(entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}
