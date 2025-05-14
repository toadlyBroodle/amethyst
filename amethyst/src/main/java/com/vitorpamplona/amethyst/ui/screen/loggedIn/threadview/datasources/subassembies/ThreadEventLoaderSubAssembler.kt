/**
 * Copyright (c) 2024 Vitor Pamplona
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.vitorpamplona.amethyst.ui.screen.loggedIn.threadview.datasources.subassembies

import com.vitorpamplona.amethyst.model.ThreadAssembler
import com.vitorpamplona.amethyst.service.relayClient.eoseManagers.PerUniqueIdEoseManager
import com.vitorpamplona.amethyst.ui.screen.loggedIn.threadview.datasources.ThreadQueryState
import com.vitorpamplona.ammolite.relays.NostrClient
import com.vitorpamplona.ammolite.relays.TypedFilter
import com.vitorpamplona.ammolite.relays.filters.EOSETime

/**
 * Loads all missing events in each thread.
 *
 * This rotates really fast with the goal to get to zero
 * events needed as fast as possible
 *
 * Each root has a relay subscription subscription for itself and
 * saves the of the thread EOSEs in the long run to avoid re-downloading
 */
class ThreadEventLoaderSubAssembler(
    client: NostrClient,
    allKeys: () -> Set<ThreadQueryState>,
) : PerUniqueIdEoseManager<ThreadQueryState>(client, allKeys, invalidateAfterEose = true) {
    override fun updateFilter(
        key: ThreadQueryState,
        since: Map<String, EOSETime>?,
    ): List<TypedFilter>? {
        val branches = ThreadAssembler().findThreadFor(key.eventId) ?: return null
        return filterMissingEventsForThread(branches, since)
    }

    override fun id(key: ThreadQueryState) = key.eventId
}
