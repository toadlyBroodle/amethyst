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
package com.vitorpamplona.quartz.nip09Deletions

import com.vitorpamplona.quartz.nip01Core.core.AddressableEvent
import com.vitorpamplona.quartz.nip01Core.core.Event
import com.vitorpamplona.quartz.nip01Core.core.HexKey
import com.vitorpamplona.quartz.nip01Core.verify
import com.vitorpamplona.quartz.utils.LargeCache

class DeletionIndex {
    data class DeletionRequest(
        val reference: String,
        val publicKey: HexKey,
    ) : Comparable<DeletionRequest> {
        override fun compareTo(other: DeletionRequest): Int {
            val compared = reference.compareTo(other.reference)

            return if (compared == 0) {
                publicKey.compareTo(publicKey)
            } else {
                compared
            }
        }
    }

    // stores a set of id OR atags (kind:pubkey:dtag) by pubkey with the created at of the deletion event.
    // Anything newer than the date should not be deleted.
    private val deletedReferencesBefore = LargeCache<DeletionRequest, DeletionEvent>()

    fun add(
        event: DeletionEvent,
        wasVerified: Boolean,
    ): Boolean {
        var atLeastOne = false
        var myWasVerified = wasVerified

        event.deleteEventIds().forEach { toDelete ->
            if (add(toDelete, event.pubKey, event, myWasVerified)) {
                myWasVerified = true
                atLeastOne = true
            }
        }

        event.deleteAddressIds().forEach { toDelete ->
            if (add(toDelete, event.pubKey, event, myWasVerified)) {
                myWasVerified = true
                atLeastOne = true
            }
        }

        return atLeastOne
    }

    private fun add(
        ref: String,
        byPubKey: HexKey,
        deletionEvent: DeletionEvent,
        wasVerified: Boolean,
    ): Boolean {
        val key = DeletionRequest(ref, byPubKey)
        val previousDeletionEvent = deletedReferencesBefore.get(key)

        if (previousDeletionEvent == null || deletionEvent.createdAt > previousDeletionEvent.createdAt) {
            if (wasVerified || deletionEvent.verify()) {
                deletedReferencesBefore.put(key, deletionEvent)
                return true
            }
        }
        return false
    }

    fun hasBeenDeletedBy(event: Event): DeletionEvent? {
        deletedReferencesBefore.get(DeletionRequest(event.id, event.pubKey))?.let {
            return it
        }

        if (event is AddressableEvent) {
            deletedReferencesBefore.get(DeletionRequest(event.addressTag(), event.pubKey))?.let {
                if (event.createdAt <= it.createdAt) {
                    return it
                }
            }
        }

        return null
    }

    fun hasBeenDeleted(event: Event): Boolean {
        val key = DeletionRequest(event.id, event.pubKey)
        if (hasBeenDeleted(key)) return true

        if (event is AddressableEvent) {
            if (hasBeenDeleted(DeletionRequest(event.addressTag(), event.pubKey), event.createdAt)) return true
        }

        return false
    }

    private fun hasBeenDeleted(key: DeletionRequest) = deletedReferencesBefore.containsKey(key)

    private fun hasBeenDeleted(
        key: DeletionRequest,
        createdAt: Long,
    ): Boolean {
        val deletionEvent = deletedReferencesBefore.get(key)
        return deletionEvent != null && createdAt <= deletionEvent.createdAt
    }

    fun size() = deletedReferencesBefore.size()
}
