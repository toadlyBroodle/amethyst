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
package com.vitorpamplona.amethyst.service.relayClient.reqCommand.account.metadata

import com.vitorpamplona.ammolite.relays.COMMON_FEED_TYPES
import com.vitorpamplona.ammolite.relays.TypedFilter
import com.vitorpamplona.ammolite.relays.filters.EOSETime
import com.vitorpamplona.ammolite.relays.filters.SincePerRelayFilter
import com.vitorpamplona.quartz.experimental.ephemChat.list.EphemeralChatListEvent
import com.vitorpamplona.quartz.nip01Core.core.HexKey
import com.vitorpamplona.quartz.nip28PublicChat.list.ChannelListEvent
import com.vitorpamplona.quartz.nip30CustomEmoji.selection.EmojiPackSelectionEvent
import com.vitorpamplona.quartz.nip51Lists.FollowListEvent
import com.vitorpamplona.quartz.nip51Lists.MuteListEvent
import com.vitorpamplona.quartz.nip51Lists.PeopleListEvent
import com.vitorpamplona.quartz.nip58Badges.BadgeProfilesEvent

fun filterFollowsAndMutesFromKey(
    pubkey: HexKey?,
    since: Map<String, EOSETime>?,
): List<TypedFilter>? {
    if (pubkey == null || pubkey.isEmpty()) return null

    return listOf(
        TypedFilter(
            types = COMMON_FEED_TYPES,
            filter =
                SincePerRelayFilter(
                    kinds =
                        listOf(
                            PeopleListEvent.KIND,
                            FollowListEvent.KIND,
                            MuteListEvent.KIND,
                            BadgeProfilesEvent.KIND,
                            EmojiPackSelectionEvent.KIND,
                            EphemeralChatListEvent.KIND,
                            ChannelListEvent.KIND,
                        ),
                    authors = listOf(pubkey),
                    limit = 100,
                    since = since,
                ),
        ),
    )
}
