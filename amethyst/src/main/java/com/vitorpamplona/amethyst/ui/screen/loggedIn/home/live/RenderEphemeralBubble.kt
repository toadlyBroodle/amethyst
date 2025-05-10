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
package com.vitorpamplona.amethyst.ui.screen.loggedIn.home.live

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitorpamplona.amethyst.model.EphemeralChatChannel
import com.vitorpamplona.amethyst.service.relayClient.reqCommand.channel.observeChannelNoteAuthors
import com.vitorpamplona.amethyst.ui.navigation.INav
import com.vitorpamplona.amethyst.ui.navigation.routeFor
import com.vitorpamplona.amethyst.ui.note.Gallery
import com.vitorpamplona.amethyst.ui.screen.loggedIn.AccountViewModel
import com.vitorpamplona.amethyst.ui.theme.StdHorzSpacer

@Composable
fun RenderEphemeralBubble(
    channel: EphemeralChatChannel,
    accountViewModel: AccountViewModel,
    nav: INav,
) {
    FilledTonalButton(
        contentPadding = PaddingValues(start = 8.dp, end = 10.dp, bottom = 0.dp, top = 0.dp),
        onClick = {
            nav.nav { routeFor(channel) }
        },
    ) {
        RenderUsers(channel, accountViewModel, nav)
        Spacer(StdHorzSpacer)
        Text(
            channel.toBestDisplayName(),
        )
    }
}

@Composable
fun RenderUsers(
    channel: EphemeralChatChannel,
    accountViewModel: AccountViewModel,
    nav: INav,
) {
    val authors by observeChannelNoteAuthors(channel, accountViewModel)

    Gallery(authors, Modifier, accountViewModel, nav, 3)
}
