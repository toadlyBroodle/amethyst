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
package com.vitorpamplona.amethyst.service.relayClient.searchCommand

import androidx.compose.runtime.Stable
import com.vitorpamplona.amethyst.model.LocalCache
import com.vitorpamplona.amethyst.service.relayClient.composeSubscriptionManagers.MutableComposeSubscriptionManager
import com.vitorpamplona.amethyst.service.relayClient.composeSubscriptionManagers.MutableQueryState
import com.vitorpamplona.amethyst.service.relayClient.searchCommand.subassemblies.SearchWatcherSubAssembler
import com.vitorpamplona.ammolite.relays.NostrClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Stable
class SearchQueryState(
    val searchQuery: MutableStateFlow<String>,
) : MutableQueryState {
    override fun flow(): Flow<String> = searchQuery
}

class SearchFilterAssembler(
    val cache: LocalCache,
    client: NostrClient,
    scope: CoroutineScope,
) : MutableComposeSubscriptionManager<SearchQueryState>(scope) {
    val group =
        listOf(
            SearchWatcherSubAssembler(cache, client, ::allKeys),
        )

    override fun start() = group.forEach { it.start() }

    override fun stop() = group.forEach { it.stop() }

    override fun invalidateFilters() = group.forEach { it.invalidateFilters() }

    override fun invalidateKeys() = invalidateFilters()

    override fun destroy() = group.forEach { it.destroy() }

    override fun printStats() = group.forEach { it.printStats() }
}
