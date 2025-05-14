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
package com.vitorpamplona.amethyst.service.relayClient.searchCommand.subassemblies

import com.vitorpamplona.ammolite.relays.FeedType
import com.vitorpamplona.ammolite.relays.TypedFilter
import com.vitorpamplona.ammolite.relays.filters.SincePerRelayFilter
import com.vitorpamplona.quartz.experimental.audio.header.AudioHeaderEvent
import com.vitorpamplona.quartz.experimental.audio.track.AudioTrackEvent
import com.vitorpamplona.quartz.experimental.interactiveStories.InteractiveStoryPrologueEvent
import com.vitorpamplona.quartz.experimental.interactiveStories.InteractiveStorySceneEvent
import com.vitorpamplona.quartz.experimental.nns.NNSEvent
import com.vitorpamplona.quartz.experimental.zapPolls.PollNoteEvent
import com.vitorpamplona.quartz.nip01Core.core.HexKey
import com.vitorpamplona.quartz.nip10Notes.TextNoteEvent
import com.vitorpamplona.quartz.nip22Comments.CommentEvent
import com.vitorpamplona.quartz.nip23LongContent.LongTextNoteEvent
import com.vitorpamplona.quartz.nip28PublicChat.admin.ChannelCreateEvent
import com.vitorpamplona.quartz.nip28PublicChat.admin.ChannelMetadataEvent
import com.vitorpamplona.quartz.nip30CustomEmoji.pack.EmojiPackEvent
import com.vitorpamplona.quartz.nip51Lists.BookmarkListEvent
import com.vitorpamplona.quartz.nip51Lists.FollowListEvent
import com.vitorpamplona.quartz.nip51Lists.PeopleListEvent
import com.vitorpamplona.quartz.nip51Lists.PinListEvent
import com.vitorpamplona.quartz.nip53LiveActivities.streaming.LiveActivitiesEvent
import com.vitorpamplona.quartz.nip54Wiki.WikiNoteEvent
import com.vitorpamplona.quartz.nip58Badges.BadgeDefinitionEvent
import com.vitorpamplona.quartz.nip72ModCommunities.definition.CommunityDefinitionEvent
import com.vitorpamplona.quartz.nip84Highlights.HighlightEvent
import com.vitorpamplona.quartz.nip99Classifieds.ClassifiedsEvent

fun searchPostsByText(searchString: HexKey) =
    listOf(
        TypedFilter(
            types = setOf(FeedType.SEARCH),
            filter =
                SincePerRelayFilter(
                    kinds =
                        listOf(
                            TextNoteEvent.KIND,
                            LongTextNoteEvent.KIND,
                            BadgeDefinitionEvent.KIND,
                            PeopleListEvent.KIND,
                            BookmarkListEvent.KIND,
                            AudioHeaderEvent.KIND,
                            AudioTrackEvent.KIND,
                            PinListEvent.KIND,
                            PollNoteEvent.KIND,
                            ChannelCreateEvent.KIND,
                        ),
                    search = searchString,
                    limit = 100,
                ),
        ),
        TypedFilter(
            types = setOf(FeedType.SEARCH),
            filter =
                SincePerRelayFilter(
                    kinds =
                        listOf(
                            ChannelMetadataEvent.KIND,
                            ClassifiedsEvent.KIND,
                            CommunityDefinitionEvent.KIND,
                            EmojiPackEvent.KIND,
                            HighlightEvent.KIND,
                            LiveActivitiesEvent.KIND,
                            PollNoteEvent.KIND,
                            NNSEvent.KIND,
                            WikiNoteEvent.KIND,
                            CommentEvent.KIND,
                        ),
                    search = searchString,
                    limit = 100,
                ),
        ),
        TypedFilter(
            types = setOf(FeedType.SEARCH),
            filter =
                SincePerRelayFilter(
                    kinds =
                        listOf(
                            InteractiveStoryPrologueEvent.KIND,
                            InteractiveStorySceneEvent.KIND,
                            FollowListEvent.KIND,
                        ),
                    search = searchString,
                    limit = 100,
                ),
        ),
    )
