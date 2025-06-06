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
package com.vitorpamplona.quartz.nip68Picture

import com.vitorpamplona.quartz.nip01Core.core.TagArrayBuilder
import com.vitorpamplona.quartz.nip23LongContent.tags.PublishedAtTag
import com.vitorpamplona.quartz.nip23LongContent.tags.TitleTag
import com.vitorpamplona.quartz.nip94FileMetadata.tags.DimensionTag
import com.vitorpamplona.quartz.nip94FileMetadata.tags.HashSha256Tag
import com.vitorpamplona.quartz.nip94FileMetadata.tags.MimeTypeTag

fun TagArrayBuilder<PictureEvent>.title(title: String) = addUnique(TitleTag.assemble(title))

fun TagArrayBuilder<PictureEvent>.summary(timestamp: Long) = addUnique(PublishedAtTag.assemble(timestamp))

fun TagArrayBuilder<PictureEvent>.pictureIMeta(
    url: String,
    mimeType: String? = null,
    blurhash: String? = null,
    dimension: DimensionTag? = null,
    hash: String? = null,
    size: Int? = null,
    alt: String? = null,
) = pictureIMeta(PictureMeta(url, mimeType, blurhash, dimension, alt, hash, size))

fun TagArrayBuilder<PictureEvent>.pictureIMeta(imeta: PictureMeta): TagArrayBuilder<PictureEvent> {
    add(imeta.toIMetaArray())
    imeta.hash?.let { add(HashSha256Tag.assemble(it)) }
    imeta.mimeType?.let { add(MimeTypeTag.assemble(it)) }
    return this
}

fun TagArrayBuilder<PictureEvent>.pictureIMetas(imageUrls: List<PictureMeta>): TagArrayBuilder<PictureEvent> {
    imageUrls.forEach { pictureIMeta(it) }
    return this
}
