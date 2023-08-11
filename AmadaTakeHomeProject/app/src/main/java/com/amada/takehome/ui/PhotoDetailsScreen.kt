package com.amada.takehome.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.amada.takehome.R
import com.amada.takehome.models.Photo
import com.amada.takehome.utils.FlickrSize
import com.amada.takehome.utils.createFlickrImageUrl
import com.amada.takehome.utils.toBoolean

@Preview
@Composable
fun PhotoDetailsContentPreview() {
    PhotoDetailsContent(
        photo = Photo(
            farm = 0,
            id = "53109042686",
            isfamily = 0,
            isfriend = 1,
            ispublic = 1,
            owner = "32227368@N08",
            secret = "d91382f92b",
            server = "65535",
            title = "Avenger"
        ),
        isFamilyLabel = "False",
        isFriendLabel = "True",
        isPublic = "True"
    )
}

@Composable
fun PopulatePhotoDetail(photoInput: Photo?) {
    val photo = photoInput ?: return
    PhotoDetailsContent(
        photo = photo,
        isFamilyLabel = stringResource(R.string.is_family),
        isFriendLabel = stringResource(R.string.is_friend),
        isPublic = stringResource(R.string.is_public)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailsContent(
    photo: Photo,
    isFamilyLabel: String,
    isFriendLabel: String,
    isPublic: String
) {
    Scaffold {
        Column(
            modifier = Modifier.padding(it)
        )
        {
            Text(
                textAlign = TextAlign.Center,
                text = photo.title,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = createFlickrImageUrl(
                    serverId = photo.server,
                    photoId = photo.id,
                    secret = photo.secret,
                    size = FlickrSize.LARGE_2048
                ),
                contentDescription = photo.title
            )
            Row {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = "$isFamilyLabel: ${photo.isfamily.toBoolean()}"
                )
            }
            Row {
                Text(
                    modifier = Modifier.padding(start = 12.dp, bottom = 12.dp),
                    text = "$isFriendLabel: ${photo.isfriend.toBoolean()}"
                )
            }
            Row {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "$isPublic: ${photo.ispublic.toBoolean()}"
                )
            }
        }
    }
}

