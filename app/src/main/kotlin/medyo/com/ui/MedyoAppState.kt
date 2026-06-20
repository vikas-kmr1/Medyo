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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import medyo.com.core.navigation.NavigationState
import medyo.com.core.navigation.rememberNavigationState
import medyo.com.bio_scan.api.BioScanNavKey
import medyo.com.feature.home.api.HomeNavKey
import medyo.com.settings.api.SettingsNavKey

@Composable
fun rememberMedyoAppState(): MedyoAppState {
    val navigationState = rememberNavigationState(
        startKey = BioScanNavKey,
        topLevelKeys = setOf(HomeNavKey, BioScanNavKey, SettingsNavKey)
    )

    return remember(navigationState) {
        MedyoAppState(navigationState = navigationState)
    }
}

@Stable
class MedyoAppState(
    val navigationState: NavigationState
)
