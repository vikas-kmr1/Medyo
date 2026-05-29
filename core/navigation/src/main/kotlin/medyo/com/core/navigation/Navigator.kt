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

import androidx.navigation3.runtime.NavKey

/**
 * ======================================================================================
 * CONCEPTUAL OVERVIEW & PSEUDO-ALGORITHMS: HOW ROUTING & BACK NAVIGATION ARE HANDLED
 * ======================================================================================
 *
 * The [Navigator] class acts as the transactional engine for routing. It intercepts 
 * request intents and manipulates the [NavigationState] backstacks directly.
 *
 * --------------------------------------------------------------------------------------
 * 1. METHOD: navigate(key)
 * --------------------------------------------------------------------------------------
 * Conceptual Pseudo-Code Flow:
 *
 * IF (key is the currently selected tab) {
 *     -> Action: Double-tap on active tab!
 *     -> Execute: Clear all nested detail screens to return to tab root. (e.g. Scanner -> BioScan)
 * } ELSE IF (key is a top-level tab) {
 *     -> Action: Switch bottom tabs!
 *     -> Execute: Adjust topLevelStack to bring the requested tab to the front.
 * } ELSE {
 *     -> Action: Deep navigation! (e.g. opening Scanner screen)
 *     -> Execute: Push key onto current active tab's sub-stack.
 * }
 *
 * --------------------------------------------------------------------------------------
 * 2. METHOD: goBack()
 * --------------------------------------------------------------------------------------
 * Conceptual Pseudo-Code Flow:
 *
 * IF (active screen is the root start destination: BioScan) {
 *     -> Action: User pressing back on start screen!
 *     -> Execute: Do nothing (let system handle exit / minimize).
 * } ELSE IF (active screen is a tab root, e.g., Settings) {
 *     -> Action: User pressing back on a top-level tab!
 *     -> Execute: Pop the tab from topLevelStack to return to the previous tab (e.g. BioScan).
 * } ELSE {
 *     -> Action: User pressing back from a detail screen! (e.g. Scanner)
 *     -> Execute: Pop the screen from the current sub-stack to reveal the screen below it.
 * }
 * ======================================================================================
 */

/**
 * Handles navigation events (forward and back) by updating the reactive navigation state.
 *
 * @param state The navigation state that will be updated in response to navigation events.
 */
class Navigator(val state: NavigationState) {

    /**
     * Navigates to a specific destination key.
     * Evaluates the destination to decide if it is a tab switch, sub-stack reset, or nested push.
     *
     * @param key The destination [NavKey] to navigate to.
     */
    fun navigate(key: NavKey) {
        when (key) {
            // Case A: The user tapped the already active tab -> clear sub-stack back to tab root
            state.currentTopLevelKey -> clearSubStack()
            
            // Case B: The user tapped a different top-level tab -> perform tab switch
            in state.topLevelKeys -> goToTopLevel(key)
            
            // Case C: The user opened a detail/nested screen -> push onto current sub-stack
            else -> goToKey(key)
        }
    }

    /**
     * Pops the current active screen or tab from the backstack.
     * Reverts to the previous screen, respecting the multi-stack tab hierarchy.
     */
    fun goBack() {
        when (state.currentKey) {
            // Case A: At the start key root -> cannot go back further (exit condition)
            state.startKey -> {
                // Exit app or let system handle it
            }
            
            // Case B: At a tab root (e.g. Settings) -> return to previous active tab
            state.currentTopLevelKey -> {
                // Pop the active tab from the topLevelStack to bring previous tab to front
                state.topLevelStack.removeLastOrNull()
            }
            
            // Case C: Deep detail screen (e.g. Scanner) -> pop back to previous screen (BioScan)
            else -> {
                state.currentSubStack.removeLastOrNull()
            }
        }
    }

    /**
     * Navigates to a nested/detail screen within the active tab.
     *
     * STEP-BY-STEP ALGORITHM:
     * 1. Get current tab's sub-stack.
     * 2. If the destination screen already exists in the stack, remove it first (avoids duplicate cycles).
     * 3. Add the screen to the end of the stack, making it the active screen.
     */
    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            // Remove it if it's already in the stack so it's added at the end.
            remove(key)
            add(key)
        }
    }

    /**
     * Switches the active bottom navigation tab.
     *
     * STEP-BY-STEP ALGORITHM:
     * 1. Check if the target tab is the default `startKey` (e.g. BioScan).
     * 2. If yes: Clear the entire topLevelStack so it's added as the only root key.
     * 3. If no: Remove any existing instance of the key in `topLevelStack` to avoid cycles.
     * 4. Add the tab to the end of the `topLevelStack` to display it.
     */
    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (key == state.startKey) {
                // This is the start key. Clear the stack so it's added as the only key.
                clear()
            } else {
                // Remove it if it's already in the stack so it's added at the end.
                remove(key)
            }
            add(key)
        }
    }

    /**
     * Clears all stacked screens on the active tab, returning the user to the tab root.
     *
     * STEP-BY-STEP ALGORITHM:
     * 1. Get current active sub-stack.
     * 2. If stack has more than 1 item (e.g. [BioScan, Scanner]):
     *    - Slice list from index 1 (just after the root tab) to the end.
     *    - Clear that slice, popping all detail screens off the active stack.
     */
    private fun clearSubStack() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }
}
