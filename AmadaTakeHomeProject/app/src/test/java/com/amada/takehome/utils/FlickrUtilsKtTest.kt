package com.amada.takehome.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FlickrUtilsKtTest {

    companion object {
    }

    @Test
    fun createFlickrImageUrl_success() {
          val flickrImageUrl = createFlickrImageUrl(
              serverId = "server_id",
              photoId = "photo_id",
              secret = "secret",
              size = FlickrSize.THUMBNAIL_150
          )

        assertEquals(flickrImageUrl,
            "https://live.staticflickr.com/server_id/photo_id_secret_q.jpg"
            )
    }
}