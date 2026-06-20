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

package medyo.com.core.navigation

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * ======================================================================================
 * CONCEPTUAL OVERVIEW & PSEUDO-EXPLANATION: HOW NAVIGATION 3 STATE WORKS IN MEDYO
 * ======================================================================================
 *
 * Traditional Navigation Compose uses string-based routes (e.g., "home", "settings/{id}")
 * and relies on a heavy runtime controller. Navigation 3 simplifies this by treating 
 * navigation as pure State Management using type-safe keys (Kotlin classes or objects).
 *
 * Medyo's navigation is structured as a "Multi-Stack" layout. Instead of a single flat
 * backstack, we maintain a dedicated backstack for each bottom-bar tab.
 *
 * Conceptually, the state behaves as follows:
 * 
 * 1. Top-Level Stack:
 *    [ HomeNavKey, SettingsNavKey, BioScanNavKey ] ---> Tracks which bottom tabs have been opened,
 *                                                       enabling back-navigation *between* tabs.
 *
 * 2. Tab Sub-Stacks (Sub-Backstacks):
 *    - Home Tab Stack:     [ HomeNavKey ]
 *    - BioScan Tab Stack:  [ BioScanNavKey, ScannerNavKey ] ---> Lets us open the Scanner as a detail
 *                                                                screen on top of the BioScan dashboard
 *                                                                without losing the Home tab's position!
 *    - Settings Tab Stack: [ SettingsNavKey ]
 *
 * 3. Active Screen Resolution:
 *    - Current Tab = topLevelStack.last() (e.g., BioScanNavKey)
 *    - Current Sub-Stack = subStacks[Current Tab]
 *    - Current Active Screen = Current Sub-Stack.last() (e.g., ScannerNavKey)
 *
 * 4. State Lifecycle Preservation:
 *    - Uses `rememberSaveableStateHolderNavEntryDecorator` to save the scroll/UI state of background tabs.
 *    - Uses `rememberViewModelStoreNavEntryDecorator` to keep ViewModels alive for inactive stacks.
 * ======================================================================================
 */

/**
 * Creates and remembers the global [NavigationState] across recompositions, configuration 
 * changes (like screen rotations), and process deaths.
 *
 * @param startKey The default starting key (e.g., [BioScanNavKey]) where the user enters the app.
 * @param topLevelKeys The complete set of valid top-level destinations (tabs) in the bottom bar.
 */
@Composable
fun rememberNavigationState(
    startKey: NavKey,
    topLevelKeys: Set<NavKey>,
): NavigationState {
    // Initialize the top-level stack with the default start key.
    val topLevelStack = rememberNavBackStack(startKey)

    // Build a map of backstacks, creating a unique backstack for each bottom tab.
    // Each backstack starts initialized with its own corresponding top-level key.
    val subStacks = topLevelKeys.associateWith { key -> rememberNavBackStack(key) }

    // Bundle them together into our NavigationState container.
    return remember(startKey, topLevelKeys) {
        NavigationState(
            startKey = startKey,
            topLevelStack = topLevelStack,
            subStacks = subStacks,
        )
    }
}

/**
 * State holder for the application's Navigation graph.
 *
 * It keeps track of the active destination and coordinates the backstacks of all tabs.
 *
 * @property startKey The absolute root tab (e.g., BioScan). The backstack is cleared if we go back to this.
 * @property topLevelStack Backstack tracking the sequence of top-level tab switches.
 * @property subStacks A map pairing each top-level [NavKey] with its own independent nested backstack.
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    /**
     * Resolves the active top-level tab (the last item in the topLevelStack).
     * Automatically wraps in derivedStateOf to recompose the UI only when the active tab shifts.
     */
    val currentTopLevelKey: NavKey by derivedStateOf { topLevelStack.last() }

    /**
     * Set of all configured top-level keys (e.g. Home, BioScan, Settings).
     */
    val topLevelKeys
        get() = subStacks.keys

    /**
     * Retrieves the backstack associated with the currently selected tab.
     */
    @get:VisibleForTesting
    val currentSubStack: NavBackStack<NavKey>
        get() = subStacks[currentTopLevelKey]
            ?: error("Sub stack for $currentTopLevelKey does not exist")

    /**
     * Resolves the absolute active screen (the last item in the current sub-stack).
     * If the current sub-stack has deep navigation (e.g. [BioScan, Scanner]), this returns `Scanner`.
     */
    @get:VisibleForTesting
    val currentKey: NavKey by derivedStateOf { currentSubStack.last() }
}

/**
 * Transforms the [NavigationState] backstacks into an active list of [NavEntry] objects 
 * that the [NavDisplay] Composable can render on screen.
 *
 * It applies lifecycle decorators to guarantee tab switching preserves state seamlessly.
 *
 * @param entryProvider A lambda that maps a [NavKey] to its respective Screen Composable UI.
 * @return A list of active, decorated screen entries ready to display.
 */
@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    // 1. Map each sub-stack to a list of decorated navigation entries.
    val decoratedEntries = subStacks.mapValues { (_, stack) ->
        // Decorators manage the state lifecycle:
        val decorators = listOf(
            // - Saves scroll positions, text field inputs, and UI state when tabs switch.
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            // - Keeps Hilt ViewModels scoped to each screen alive in the background.
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        )
        
        // Assemble and decorate the backstack items using Navigation 3 runtime.
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = decorators,
            entryProvider = entryProvider,
        )
    }

    // 2. Flatten the entries in order of the active top-level sequence.
    //    This creates a single sequential state list representing the visible screen layer.
    return topLevelStack
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
