/**
 * Copyright (c) 2025 Vitor Pamplona
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
package com.vitorpamplona.quartz.nip01Core.core

import androidx.compose.runtime.Immutable
import com.fasterxml.jackson.annotation.JsonProperty
import com.vitorpamplona.quartz.nip01Core.jackson.EventManualSerializer
import com.vitorpamplona.quartz.nip01Core.jackson.EventMapper
import com.vitorpamplona.quartz.nip01Core.signers.eventTemplate
import com.vitorpamplona.quartz.utils.TimeUtils
import com.vitorpamplona.quartz.utils.bytesUsedInMemory
import com.vitorpamplona.quartz.utils.pointerSizeInBytes

@Immutable
open class Event(
    val id: HexKey,
    @JsonProperty("pubkey") val pubKey: HexKey,
    @JsonProperty("created_at") val createdAt: Long,
    val kind: Int,
    val tags: TagArray,
    val content: String,
    val sig: HexKey,
) : IEvent {
    open fun isContentEncoded() = false

    open fun countMemory(): Long =
        7 * pointerSizeInBytes + // 7 fields, 4 bytes each reference (32bit)
            12L + // createdAt + kind
            id.bytesUsedInMemory() +
            pubKey.bytesUsedInMemory() +
            tags.sumOf { pointerSizeInBytes + it.sumOf { pointerSizeInBytes + it.bytesUsedInMemory() } } +
            content.bytesUsedInMemory() +
            sig.bytesUsedInMemory()

    fun toJson(): String = EventManualSerializer.toJson(id, pubKey, createdAt, kind, tags, content, sig)

    /**
     * For debug purposes only
     */
    fun toPrettyJson(): String = EventManualSerializer.toPrettyJson(id, pubKey, createdAt, kind, tags, content, sig)

    companion object {
        fun fromJson(json: String): Event = EventMapper.fromJson(json)

        fun build(
            kind: Int,
            content: String = "",
            createdAt: Long = TimeUtils.now(),
            initializer: TagArrayBuilder<Event>.() -> Unit = {},
        ) = eventTemplate(kind, content, createdAt, initializer)
    }
}
