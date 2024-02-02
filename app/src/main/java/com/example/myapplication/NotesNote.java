package com.example.myapplication;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;

public class NotesNote {
    public static ArrayList<NotesNote> noteArrayList = new ArrayList<>();
    public static String NOTE_EDIT_EXTRA =  "noteEdit";


    private int id;
    private String title;
    private String cont;
    private boolean isFavourite;
    private String lastOpened;
    private Date deleted;

    public NotesNote(int id, String title, String cont, boolean isFavourite, String lastOpened, Date deleted) {
        this.id = id;
        this.title = title;
        this.cont = cont;
        this.isFavourite = isFavourite;
        this.lastOpened = lastOpened;
        this.deleted = deleted;
    }

    public NotesNote(int id, String title, String cont) {
        this.id = id;
        this.title = title;
        this.cont = cont;
        this.deleted = null;
    }

    public NotesNote() {

    }


    public static NotesNote getNoteForID(int passedNoteID)
    {
        for (NotesNote note : noteArrayList)
        {
            if(note.getId() == passedNoteID)
                return note;
        }

        return null;
    }

    public static ArrayList<NotesNote> nonDeletedNotes()
    {
        ArrayList<NotesNote> nonDeleted = new ArrayList<>();
        for(NotesNote note : noteArrayList)
        {
            if(note.getDeleted() == null)
                nonDeleted.add(note);
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getLastOpened() {
        return lastOpened;
    }

    public void setLastOpened(String lastOpened) {
        this.lastOpened = lastOpened;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}