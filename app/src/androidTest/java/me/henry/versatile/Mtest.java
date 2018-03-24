package me.henry.versatile;

import android.test.AndroidTestCase;

import org.junit.Test;

import me.henry.versatile.db.DBManager;
import me.henry.versatile.base.DatabaseHelper;
import me.henry.versatile.model.note.Note;

/**
 * Created by henry on 2018/1/9.
 */

public class Mtest extends AndroidTestCase {
    @Test
    public void initDatabase() {
        DBManager.init(getContext(), new DatabaseHelper(getContext()));

    }
    @Test
    public void delete(){
        Note note = new Note();
       note.setId(1);
        DBManager.getInstance().getDao(Note.class).delete(note);
    }
}
