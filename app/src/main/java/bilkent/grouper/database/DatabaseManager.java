package bilkent.grouper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.HashMap;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.classes.Group;

public class DatabaseManager extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
   private static final String DATABASE_NAME = LoginActivity.currentUser.getID() + "_db";

   // Database Columns
    private static final String TABLE_NAME = "USER_GROUPS";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String GROUP_PHOTO = "GROUP_PHOTO";



   public DatabaseManager(Context context){
       super(context, DATABASE_NAME,null, DATABASE_VERSION);

   }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
               + GROUP_ID + " TEXT,"
               + GROUP_NAME + " TEXT,"
               + GROUP_PHOTO + " TEXT)";
       db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addGroup(Group group){
       SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROUP_ID, group.getGroupID());
        values.put(GROUP_NAME, group.getGroupName());
        values.put(GROUP_PHOTO, group.getPhoto());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Group getGroup(String groupID){
       Group group = new Group();
       String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + GROUP_ID + "='" + groupID;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       cursor.moveToFirst();
       if (cursor.getCount() > 0) {
           group.setGroupID(cursor.getString(1));
           group.setGroupName(cursor.getString(2));
           if (cursor.getString(3) != null)
                group.setPhoto(cursor.getString(3));
       }
       cursor.close();
       db.close();
       return group;
    }
}
