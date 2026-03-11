import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color


data class Note(
  val id: Int,
  val content: String,
  val updatedAt: Long
)


@OptIn(ExperimentalResourceApi::class)
// @Composable
// fun App() {
//     MaterialTheme {
//         var greetingText by remember { mutableStateOf("あああああああ") }
//         var showImage by remember { mutableStateOf(false) }

//         Column(
//             Modifier.fillMaxWidth(),
//             horizontalAlignment = Alignment.CenterHorizontally
//         ) {

//             // 1つ目のボタン
//             Button(onClick = {
//                 greetingText = "Hello, ${getPlatformName()}"
//                 showImage = !showImage
//             }) {
//                 Text(greetingText)
//             }

//             // 2つ目のボタン（追加）
//             Button(onClick = {
//                 greetingText = "2つ目のボタンが押されました"
//             }) {
//                 Text("別のボタン")
//             }

//             AnimatedVisibility(showImage) {
//                 Image(
//                     painterResource("compose-multiplatform.xml"),
//                     contentDescription = "Compose Multiplatform icon"
//                 )
//             }
//         }
//     }
// }

@Composable
fun App() {
  MaterialTheme {

    var notes by remember { mutableStateOf(listOf<Note>()) }
    var selectedNoteId by remember { mutableStateOf<Int?>(null) }

    Row(Modifier.fillMaxSize()) {

      NotesList(
        notes = notes,
        selectedNoteId = selectedNoteId,
        onSelect = { id -> selectedNoteId = id },
        onAdd = {
          val newId = (notes.maxOfOrNull { it.id } ?: 0) + 1
          val newNote = Note(
            id = newId,
            content = "",
            updatedAt = System.currentTimeMillis()
          )
          notes = notes + newNote
          selectedNoteId = newId
        },
        modifier = Modifier
          .fillMaxHeight()
          .width(250.dp)
          .background(Color(0xFFF5F5F5))
      )

      NoteEditor(
        note = notes.find { it.id == selectedNoteId },
        onUpdate = { updatedNote ->
          notes = notes.map { if (it.id == updatedNote.id) updatedNote else it }
        },
        onDelete = { id ->
          notes = notes.filterNot { it.id == id }

          // 削除したノートが選択中だった場合
          selectedNoteId = notes.lastOrNull()?.id
        },
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White)
      )
    }
  }
}

@Composable
fun NotesList(
  notes: List<Note>,
  selectedNoteId: Int?,
  onSelect: (Int) -> Unit,
  onAdd: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier.padding(16.dp)) {

    Row(
      Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("Notes", style = MaterialTheme.typography.h6)
      Button(onClick = onAdd) {
        Text("Add")
      }
    }

    Spacer(Modifier.height(16.dp))

    notes.forEach { note ->
      val isSelected = note.id == selectedNoteId

      Box(
        Modifier
          .fillMaxWidth()
          .height(50.dp)
          .background(if (isSelected) Color.Gray else Color.LightGray)
          .clickable { onSelect(note.id) }
          .padding(8.dp)
      ) {
        Text(
          text = note.content.lineSequence().firstOrNull()?.take(25) ?: "(No Title)",
          color = if (isSelected) Color.White else Color.Black
        )
      }

      Spacer(Modifier.height(8.dp))
    }
  }
}



@Composable
fun NoteEditor(
  note: Note?,
  onUpdate: (Note) -> Unit,
  onDelete: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  if (note == null) {
    Box(modifier) {
      Text("ノートが選択されていません", Modifier.align(Alignment.Center))
    }
    return
  }

  // ノートが切り替わったら内容も切り替わるようにする
  var text by remember(note.id) { mutableStateOf(note.content) }

  Column(modifier.padding(16.dp)) {

    Row(
      Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("Note!!", style = MaterialTheme.typography.h5)
      Button(onClick = { onDelete(note.id) }) {
        Text("Delete")
      }
    }

    Spacer(Modifier.height(16.dp))

    TextField(
      value = text,
      onValueChange = {
        text = it
        onUpdate(
          note.copy(
            content = it,
            updatedAt = System.currentTimeMillis()
          )
        )
      },
      modifier = Modifier.fillMaxSize()
    )
  }
}

expect fun getPlatformName(): String