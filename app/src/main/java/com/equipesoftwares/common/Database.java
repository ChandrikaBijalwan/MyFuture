package com.equipesoftwares.common;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.equipesoftwares.Pojo.CommonIdValuePojo;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {

	Context context;
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	SQLiteDatabase db;
	// The Android's default system path of your application database.

	// Database Name
	public static String DB_NAME = "BoardInfoDB";
	public static String DB_PATH = "";

	// Board Detail table field
	private static final String TABLE_BOARDS = "table_board";
	public static final String BOARD_ID = "board_id";
	public static final String BOARD_NAME = "board_name";
	//management table
	private static final String TABLE_MANAGEMENT = "table_management";
	public static final String MANAGEMENT_ID = "management_id";
	public static final String MANAGEMENT_NAME = "management_name";

	private String CREATE_TABLE_BOARD = "CREATE TABLE "+TABLE_BOARDS+ "("+
			BOARD_ID +  " INTEGER PRIMARY KEY, " +
			BOARD_NAME + " VARCHAR )";
	private String CREATE_TABLE_MANAGEMENT = "CREATE TABLE "+TABLE_MANAGEMENT+ "("+
			MANAGEMENT_ID +  " INTEGER PRIMARY KEY, " +
			MANAGEMENT_NAME + " VARCHAR )";


	public Database(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_BOARD);
		db.execSQL(CREATE_TABLE_MANAGEMENT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void openDB() {
		db = this.getWritableDatabase();
	}

	public void closeDB() {
		db.close();
	}



	// Add all board  to db
	public void addAllBoardsToDB(ArrayList<CommonIdValuePojo> arraylistPojo){
		try {
			openDB();
//			arraylistPojo.add(0, new CommonIdValuePojo(0, "All"));
			for (int i = 0; i < arraylistPojo.size(); i++) {
				ContentValues cv = new ContentValues();
				cv.put(BOARD_ID, arraylistPojo.get(i).getKey() + "");
				cv.put(BOARD_NAME, arraylistPojo.get(i).getValue());
				long rowId = db.insert(TABLE_BOARDS, null, cv);
				//Utils.printLog("cv:for board", cv + "");
			}
			close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	// Add all management  to db
	public void addAllMangementToDB(ArrayList<CommonIdValuePojo> arraylistPojo){
		try {
			openDB();
			 db.delete(TABLE_MANAGEMENT, null, null);
			//arraylistPojo.add(0, new CommonIdValuePojo(0, "All"));
			for (int i = 0; i < arraylistPojo.size(); i++) {
				ContentValues cv = new ContentValues();
				cv.put(MANAGEMENT_ID, arraylistPojo.get(i).getKey() + "");
				cv.put(MANAGEMENT_NAME, arraylistPojo.get(i).getValue());
				long rowId2 = db.insert(TABLE_MANAGEMENT, null, cv);
				Utils.printLog("cv: for management", cv + "");
			}
			close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
		public long getManagementCount() {
			String countQuery = "SELECT  * FROM " + TABLE_MANAGEMENT;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int cnt = cursor.getCount();
			cursor.close();
			return cnt;
		}
                //get management data
            public ArrayList<CommonIdValuePojo> getManagement() {

                openDB();
                ArrayList<CommonIdValuePojo> arrManagements = new ArrayList<CommonIdValuePojo>();
                if(arrManagements.size()>0)
                	arrManagements.clear();
                Cursor crManagements = db.query(TABLE_MANAGEMENT, null, null, null, null, null, null);
                Utils.printLog("crManagements.getCount", crManagements.getCount() + "");
                if(crManagements ==null && crManagements.getCount()<0){
                    crManagements.moveToFirst();
                    for (int i = 0; i < crManagements.getCount(); i++) {
                        arrManagements.add(new CommonIdValuePojo(crManagements.getInt(crManagements.getColumnIndex(MANAGEMENT_ID)), crManagements.getString(crManagements.getColumnIndex(MANAGEMENT_NAME))));
                        crManagements.moveToNext();
                    }
                    crManagements.close();
                }
                closeDB();
                return arrManagements;

            }
	//get all users
	public ArrayList<CommonIdValuePojo> getBoards() {
		openDB();
		ArrayList<CommonIdValuePojo> arrBoards = new ArrayList<CommonIdValuePojo>();
		if(arrBoards.size()>0)
			arrBoards.clear();
		Cursor crBoards = db.query(TABLE_BOARDS, null, null, null, null, null, null);
		Utils.printLog("cursor boards.getCount", crBoards.getCount() + "");
		if(crBoards ==null && crBoards.getCount()>0){
			crBoards.moveToFirst();
			for (int i = 0; i < crBoards.getCount(); i++) {
				arrBoards.add(new CommonIdValuePojo(crBoards.getInt(crBoards.getColumnIndex(BOARD_ID)), crBoards.getString(crBoards.getColumnIndex(BOARD_NAME))));
				crBoards.moveToNext();

			}
			crBoards.close();
		}
		closeDB();
		return arrBoards;

	}
	public String getBoardValue(int board_id) {
		String board_name="";
		String selectQuery = "SELECT  * FROM " + TABLE_BOARDS + " where " + BOARD_ID + "=" + board_id;
		db = this.getReadableDatabase();
		// 2. get reference to writable DB

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
				board_name = c.getString(c.getColumnIndex(BOARD_NAME));
			} while (c.moveToNext());
		}


		return board_name;
	}

	//clear all users
	public int clearData() {
		openDB();
		int deletionResult = db.delete(TABLE_BOARDS, null, null);
		closeDB();
		return deletionResult;

	}

}
