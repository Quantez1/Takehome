package com.amada.takehome.utils

enum class FlickrSize(val suffix: String) {
    THUMBNAIL_150("q"),
}

/**
 * This creates a Flickr image URL
 *
 * See https://www.flickr.com/services/api/misc.urls.html
 */
fun createFlickrImageUrl(
    serverId: String,
    photoId: String,
    secret: String,
    size: FlickrSize
): String {
    return "https://live.staticflickr.com/$serverId/${photoId}_${secret}_${size.suffix}.jpg"
}


/*
s	thumbnail	75	cropped square
q	thumbnail	150	cropped square
t	thumbnail	100
m	small	240
n	small	320
w	small	400
(none)	medium	500
z	medium	640
c	medium	800
b	large	1024
h	large	1600	has a unique secret; photo owner can restrict
k	large	2048	has a unique secret; photo owner can restrict
3k	extra large	3072	has a unique secret; photo owner can restrict
4k	extra large	4096	has a unique secret; photo owner can restrict
f	extra large	4096	has a unique secret; photo owner can restrict; only exists for 2:1 aspect ratio photos
5k	extra large	5120	has a unique secret; photo owner can restrict
6k	extra large	6144	has a unique secret; photo owner can restrict
o	original	arbitrary
 */