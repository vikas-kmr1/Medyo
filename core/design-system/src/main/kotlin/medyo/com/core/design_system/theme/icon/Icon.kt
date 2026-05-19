package medyo.com.core.design_system.theme.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ShortText
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material.icons.rounded.ViewDay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class MedyoIcon(
    val icon: ImageVector,
    val iconTint: Color = Color.Unspecified,
    val contentDescription: String? = null,
)

object MedyoIcons {
    val Add = MedyoIcon(icon = Icons.Rounded.Add)
    val ArrowBack = MedyoIcon(Icons.AutoMirrored.Rounded.ArrowBack)
    val seemore = MedyoIcon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "See More")
    val Bookmark = MedyoIcon(Icons.Rounded.Bookmark)
    val BookmarkBorder = MedyoIcon(Icons.Rounded.BookmarkBorder)
    val Bookmarks = MedyoIcon(Icons.Rounded.Bookmarks)
    val BookmarksBorder = MedyoIcon(Icons.Outlined.Bookmarks)
    val Check = MedyoIcon(Icons.Rounded.Check)
    val Close = MedyoIcon(Icons.Rounded.Close)
    val Grid3x3 = MedyoIcon(Icons.Rounded.Grid3x3)
    val MoreVert = MedyoIcon(Icons.Default.MoreVert)
    val Person = MedyoIcon(Icons.Rounded.Person)
    val Search = MedyoIcon(Icons.Rounded.Search)
    val Settings = MedyoIcon(Icons.Rounded.Settings)
    val ShortText = MedyoIcon(Icons.AutoMirrored.Rounded.ShortText)
    val Upcoming = MedyoIcon(Icons.Rounded.Upcoming)
    val UpcomingBorder = MedyoIcon(Icons.Outlined.Upcoming)
    val ViewDay = MedyoIcon(Icons.Rounded.ViewDay)
}
