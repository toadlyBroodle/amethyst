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
package com.vitorpamplona.amethyst.ui.screen.loggedIn.home.datasource.nip65Follows

import com.vitorpamplona.ammolite.relays.FeedType
import com.vitorpamplona.ammolite.relays.TypedFilter
import com.vitorpamplona.ammolite.relays.filters.EOSETime
import com.vitorpamplona.ammolite.relays.filters.SinceAuthorPerRelayFilter
import com.vitorpamplona.quartz.experimental.audio.header.AudioHeaderEvent
import com.vitorpamplona.quartz.experimental.audio.track.AudioTrackEvent
import com.vitorpamplona.quartz.experimental.ephemChat.chat.EphemeralChatEvent
import com.vitorpamplona.quartz.experimental.interactiveStories.InteractiveStoryPrologueEvent
import com.vitorpamplona.quartz.experimental.zapPolls.PollNoteEvent
import com.vitorpamplona.quartz.nip01Core.core.HexKey
import com.vitorpamplona.quartz.nip10Notes.TextNoteEvent
import com.vitorpamplona.quartz.nip18Reposts.GenericRepostEvent
import com.vitorpamplona.quartz.nip18Reposts.RepostEvent
import com.vitorpamplona.quartz.nip23LongContent.LongTextNoteEvent
import com.vitorpamplona.quartz.nip51Lists.PinListEvent
import com.vitorpamplona.quartz.nip53LiveActivities.chat.LiveActivitiesChatMessageEvent
import com.vitorpamplona.quartz.nip53LiveActivities.streaming.LiveActivitiesEvent
import com.vitorpamplona.quartz.nip54Wiki.WikiNoteEvent
import com.vitorpamplona.quartz.nip84Highlights.HighlightEvent
import com.vitorpamplona.quartz.nip99Classifieds.ClassifiedsEvent

fun filterHomePostsByFollows(
    follows: Map<String, List<HexKey>>?,
    since: Map<String, EOSETime>?,
): List<TypedFilter> {
    if (follows != null && follows.isEmpty()) return emptyList()

    return listOf(
        TypedFilter(
            types = setOf(if (follows == null) FeedType.GLOBAL else FeedType.FOLLOWS),
            filter =
                SinceAuthorPerRelayFilter(
                    kinds =
                        listOf(
                            TextNoteEvent.KIND,
                            RepostEvent.KIND,
                            GenericRepostEvent.KIND,
                            ClassifiedsEvent.KIND,
                            LongTextNoteEvent.KIND,
                            EphemeralChatEvent.KIND,
                            HighlightEvent.KIND,
                        ),
                    authors = follows,
                    limit = 400,
                    since = since,
                ),
        ),
        TypedFilter(
            types = setOf(if (follows == null) FeedType.GLOBAL else FeedType.FOLLOWS),
            filter =
                SinceAuthorPerRelayFilter(
                    kinds =
                        listOf(
                            PollNoteEvent.KIND,
                            AudioTrackEvent.KIND,
                            AudioHeaderEvent.KIND,
                            PinListEvent.KIND,
                            InteractiveStoryPrologueEvent.KIND,
                            LiveActivitiesChatMessageEvent.KIND,
                            LiveActivitiesEvent.KIND,
                            WikiNoteEvent.KIND,
                        ),
                    authors = follows,
                    limit = 400,
                    since = since,
                ),
        ),
    )
}
