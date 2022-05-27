package com.saklayen.techhome.note;

import android.app.Application;

import androidx.lifecycle.LiveData;


import com.saklayen.techhome.models.Note;
import com.saklayen.techhome.room.dao.NoteDao;
import com.saklayen.techhome.room.database.Device_Operation_AsyncTask;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDataBase dataBase = NoteDataBase.getInstance(application);
        noteDao = dataBase.noteDao();
        allNotes = noteDao.getAllNotes();
    }


    public void insert(Note note){
        new Device_Operation_AsyncTask(noteDao, Utils.INSERT_OPERATION).execute(note);
    }
    public void update(Note note){
        new Device_Operation_AsyncTask(noteDao, Utils.UPDATE_OPERATION).execute(note);
    }
    public void delete(Note note){
        new Device_Operation_AsyncTask(noteDao, Utils.DELETE_OPERATION).execute(note);
    }
    public void deleteAllNotes(){
        new Device_Operation_AsyncTask(noteDao, Utils.DELETE_ALL_OPERATION).execute();

    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}
