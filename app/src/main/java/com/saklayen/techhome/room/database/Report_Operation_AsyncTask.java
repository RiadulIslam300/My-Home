package com.saklayen.techhome.room.database;

import android.os.AsyncTask;

import com.saklayen.techhome.models.Note;
import com.saklayen.techhome.room.dao.NoteDao;
import com.saklayen.techhome.note.Utils;

public class Report_Operation_AsyncTask extends AsyncTask<Note,Void,Void> {
    private NoteDao noteDao;
    private int  dbOperationType;

    public Report_Operation_AsyncTask(NoteDao noteDao, int dbOperaionType) {
        this.noteDao = noteDao;
        this.dbOperationType= dbOperaionType;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        switch (dbOperationType){
            case Utils.INSERT_OPERATION:
                noteDao.insert(notes[0]);
                break;
            case Utils.DELETE_OPERATION:
                noteDao.delete(notes[0]);
                break;
            case Utils.UPDATE_OPERATION:
                noteDao.update(notes[0]);
                break;
            case Utils.DELETE_ALL_OPERATION:
                noteDao.deleteAllNotes();
                break;
            default:break;
        }
        return null;
    }
}
