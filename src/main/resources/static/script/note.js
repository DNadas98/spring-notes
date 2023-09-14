import NoteService from "./noteService.js";

class NoteController {
  constructor() {
    this.noteId = new URLSearchParams(window.location.search).get("id");
    console.log(this.noteId);
  }

  async loadNoteById() {
    try {
      const result = await NoteService.getNoteById(this.noteId);
      const note = result?.data;
      if (note) {
        this.renderNote(note);
      } else {
        throw new Error(result?.error ?? `Failed to load note with ID ${this.noteId}`);
      }
    } catch (e) {
      console.error(e);
    }
  }

  async handleSubmit(event) {
    try {
      event.preventDefault();
      const note = {
        title: document.getElementById("noteTitleInput").value,
        content: document.getElementById("noteContentInput").value
      };
      if (this.noteId) {
        note.id = this.noteId;
      }
      const result = await NoteService.saveNote(note);
      if (result?.message) {
        window.alert(result.message);
        window.location.href = "/";
      } else if (result?.error) {
        window.alert(result.error);
      } else {
        throw new Error(`Failed to ${this.noteId ? "add" : "update"} note with ID ${this.noteId}`);
      }
    } catch (e) {
      console.error(e);
    }
  }

  renderNote(note) {
    document.getElementById("noteTitleInput").value = note.title;
    document.getElementById("noteContentInput").value = note.content;
  }

  init() {
    document.getElementById("noteFormTitle").innerHTML = this.noteId ? "Update note" : "Add new note";

    if (this.noteId) {
      this.loadNoteById();
    }

    document.getElementById("noteForm").addEventListener("submit", (event) => {
      this.handleSubmit(event);
    });
  }
}

const noteController = new NoteController();
window.onload = () => noteController.init();
