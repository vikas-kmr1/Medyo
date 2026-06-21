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

package medyo.com.feature.detail.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import medyo.com.core.navigation.Navigator
import medyo.com.feature.detail.api.MedicationDetailNavKey
import medyo.com.feature.detail.impl.MedicationDetailScreen


fun EntryProviderScope<NavKey>.MedicationDetailEntry(navigator: Navigator) {
    entry<MedicationDetailNavKey> {medication ->
        MedicationDetailScreen(
            medicationId = medication.medicationId)
    }
}
