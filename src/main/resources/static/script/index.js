import NoteService from "./noteService.js";

class IndexController {
  constructor() {
    this.notes = [];
  }

  async loadAllNotes() {
    try {
      const result = await NoteService.getAllNotes();
      if (result?.error) {
        throw new Error(result.error);
      }
      this.notes = result?.data || [];
      document.getElementById("notesSearchInput").value = "";
      this.renderNotes();
    } catch (e) {
      console.error(e);
    }
  }

  handleSearch(event) {
    const searchInput = event.target.value.toLowerCase();
    this.renderNotes(this.notes.filter((note) => note.title.toLowerCase().includes(searchInput)));
  }

  async handleDelete(id) {
    try {
      const confirmed = window.confirm(`Are you sure you want to delete note with ID ${id}?`);
      if (!confirmed) {
        return;
      }

      const result = await NoteService.deleteNote(id);
      if (result?.message) {
        window.alert(result.message);
        await this.loadAllNotes();
      } else {
        throw new Error(result?.error ?? `Failed to delete note with ID ${id}`);
      }
    } catch (e) {
      window.alert(e.message);
    }
  }

  renderNotes(filteredNotes = this.notes) {
    const notesDiv = document.getElementById("notesDiv");
    if (filteredNotes.length === 0) {
      notesDiv.innerHTML = `<h3>No notes found</h3>`;
      return;
    }

    notesDiv.innerHTML = `
      <table>
        <tbody id="notesTableBody"></tbody>
      </table>`;
    const tbody = document.getElementById("notesTableBody");

    filteredNotes
      .forEach((note) => this.createNoteTableRow(tbody, note));
  }

  createNoteTableRow(tbody, note) {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${note.title}</td>
      <td>${new Date(note.created_at).toLocaleDateString()}</td>
      <td><a href="/note.html?id=${note.id}" tabindex="-1"><button>Open</button></a></td>
      <td><button id="noteDeleteButton-${note.id}">Delete</button></td>`;
    tbody.appendChild(tr);
    document.getElementById(`noteDeleteButton-${note.id}`).addEventListener("click", () => {
      indexController.handleDelete(note.id);
    });
  }

  init() {
    this.loadAllNotes();
    document.getElementById("notesSearchInput").addEventListener("input", (event) => {
      this.handleSearch(event);
    });
  }
}

const indexController = new IndexController();
window.onload = () => indexController.init();