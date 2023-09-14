export default class NoteService {
  static async getAllNotes() {
    const response = await fetch("/api/notes");
    return response.json();
  }

  static async getNoteById(id) {
    const response = await fetch(`/api/notes/${id}`);
    return response.json();
  }

  static async saveNote(note) {
    const response = await fetch("/api/notes", {
      method: note?.id ? "PATCH" : "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(note)
    });
    return response.json();
  }

  static async deleteNote(id) {
    const response = await fetch(`/api/notes/${id}`, {
      method: "DELETE"
    });
    return response.json();
  }
}