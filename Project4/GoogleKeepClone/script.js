//json-server --watch db.json comanda pt json server

let notes = [];

function saveNote() {
  const titleInput = document.querySelector('.new-note input');
  const contentInput = document.querySelector('.new-note textarea');
  const colorInput = document.getElementById('new-note-color');
  const imageInput = document.getElementById('new-note-image');

  const title = titleInput.value;
  const content = contentInput.value;
  const backgroundColor = colorInput.value;
  const imageFile = imageInput.files[0];
  const imageUrl = imageFile ? URL.createObjectURL(imageFile) : '';
  const id = generateId();

  const note = {
    id: id,
    title: title,
    content: content,
    backgroundColor: backgroundColor,
    imageUrl: imageUrl,
  };
  if (note.title === '' && note.content === '') {
    return;
  }
  fetch('http://localhost:3000/notes', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(note),
  })
    .then((response) => response.json())
    .then((data) => {
      notes.push(data);
      displayNotes();
    })
    .catch((error) => {
      console.error('Error saving note:', error);
    });

  titleInput.value = '';
  contentInput.value = '';
  colorInput.value = '#ffffff';
  imageInput.value = '';
}

document.addEventListener('click', (event) => {
  const target = event.target;

  if (!target.closest('.note') && !target.closest('.new-note')) {
    saveNote();
  }
});

function displayNotes() {
  const notesContainer = document.querySelector('.notes-container');
  notesContainer.innerHTML = '';

  const searchInput = document.getElementById('search');
  const searchTerm = searchInput.value.toLowerCase();

  fetch('http://localhost:3000/notes')
    .then((response) => response.json())
    .then((data) => {
      notes = data;
    })
    .catch((error) => {
      console.error('Error fetching notes:', error);
    });
  for (let i = notes.length - 1; i >= 0; i--) {
    const note = notes[i];

    if (
      note.title.toLowerCase().includes(searchTerm) ||
      note.content.toLowerCase().includes(searchTerm)
    ) {
      const noteElement = document.createElement('div');
      noteElement.classList.add('note');
      noteElement.style.backgroundColor = note.backgroundColor;

      if (note.imageUrl) {
        noteElement.style.backgroundImage = `url(${note.imageUrl})`;
      }

      const titleElement = document.createElement('h2');
      titleElement.textContent = note.title;
      titleElement.contentEditable = true;
      titleElement.addEventListener('blur', () => {
        note.title = titleElement.textContent;
      });

      const contentElement = document.createElement('p');
      contentElement.textContent = note.content;
      contentElement.contentEditable = true;
      contentElement.addEventListener('blur', () => {
        note.content = contentElement.textContent;
      });

      const deleteButton = document.createElement('button');
      deleteButton.innerText = 'Delete';
      deleteButton.addEventListener('click', () => {
        console.log(notes[i].id);
        updateNotesOnServer(i - 1);
        deleteNoteByIndex(i);

        const overlay = document.querySelector('.overlay');
        if (overlay.style.display === 'block') {
          overlay.style.display = 'none';
        }
      });

      const colorElement = document.createElement('input');
      colorElement.type = 'color';
      colorElement.value = note.backgroundColor;
      colorElement.addEventListener('input', () => {
        note.backgroundColor = colorElement.value;
        noteElement.style.backgroundColor = colorElement.value;
      });

      const imageElement = document.createElement('input');
      imageElement.type = 'file';
      imageElement.accept = 'image/*';
      imageElement.addEventListener('change', () => {
        const imageFile = imageElement.files[0];
        note.imageUrl = imageFile ? URL.createObjectURL(imageFile) : '';
        noteElement.style.backgroundImage = note.imageUrl
          ? `url(${note.imageUrl})`
          : '';
      });

      noteElement.addEventListener('click', (event) => {
        if (event.target !== deleteButton) {
          noteElement.classList.toggle('expanded', true);
          const overlay = document.querySelector('.overlay');
          overlay.style.display = 'block';
          if (!document.querySelector('.cancel')) {
            const cancelButton = document.createElement('button');
            cancelButton.classList.add('cancel');
            cancelButton.innerText = 'Cancel';
            noteElement.appendChild(cancelButton);

            cancelButton.addEventListener('click', (event) => {
              note.title = titleElement.textContent;
              note.content = contentElement.textContent;
              note.backgroundColor = colorElement.value;
              note.imageUrl = imageElement.files[0]
                ? URL.createObjectURL(imageElement.files[0])
                : '';
              updateNoteOnServer(note);
              event.stopPropagation();
              noteElement.classList.remove('expanded');
              overlay.style.display = 'none';
              noteElement.removeChild(cancelButton);
            });
          }
        }
      });
      noteElement.appendChild(titleElement);
      noteElement.appendChild(contentElement);
      notesContainer.appendChild(noteElement);
      noteElement.appendChild(deleteButton);
      noteElement.appendChild(colorElement);
      noteElement.appendChild(imageElement);
    }
  }
}

updateNotesOnServer = (index) => {
  fetch(`http://localhost:3000/notes/${notes[index + 1].id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then((response) => response.json())
    .then((data) => console.log(data))
    .catch((error) => console.log(error));
};

deleteNoteByIndex = (index) => {
  notes.splice(index, 1);
  displayNotes();
};

const searchInput = document.getElementById('search');
searchInput.addEventListener('input', displayNotes);

function toggleExpanded() {
  const newNote = document.getElementById('new-note');
  const newNoteHeader = document.querySelector('.new-note-header');
  const newNoteBody = document.querySelector('.new-note-body');
  const isExpanded = newNote.classList.contains('expanded');

  if (isExpanded) {
    newNote.classList.remove('expanded');
  } else {
    newNote.classList.add('expanded');

    document.addEventListener('click', function (event) {
      const isClickedInside = newNote.contains(event.target);

      if (!isClickedInside) {
        newNote.classList.remove('expanded');
      }
    });
  }
}
document.addEventListener('DOMContentLoaded', () => {
  fetch('http://localhost:3000/notes')
    .then((response) => response.json())
    .then((notesData) => {
      notes = notesData;
      displayNotes();
    })
    .catch((error) => console.error('Error fetching notes:', error));
});
function generateId() {
  return '_' + Math.random().toString(36).substr(2, 9);
}

function updateNoteOnServer(note) {
  console.log(note.id);
  fetch(`http://localhost:3000/notes/${note.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(note),
  })
    .then((response) => response.json())
    .then((data) => console.log(data))
    .catch((error) => console.error(error));
}
