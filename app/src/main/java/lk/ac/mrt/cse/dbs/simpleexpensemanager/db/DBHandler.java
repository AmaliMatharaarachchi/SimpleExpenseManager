package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 2016-11-20.
 */
public class DBHandler extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "simpleExpenseManager.db";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE account" +
            "(" +
            "account_no int," +
            "bank varchar(20)," +
            "acc_holder varchar(20) not null," +
            "balance numeric(12,2)," +
            "CONSTRAINT pk_account_id PRIMARY KEY (account_no)" +
            ")";
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE transaction" +
            "(" +
            "transaction_date date not null," +
            "account_no int not null," +
            "expense_type BOOLEAN not null," +
            "amount numeric(12,2) not null," +
            "FOREIGN KEY (account_no) REFERENCES account(account_no)" +
            ")";

    private static final String DELETE_ACCOUNT_ENTRIES =
            "DROP TABLE IF EXISTS account";
    private static final String DELETE_TRANSACTION_ENTRIES =
            "DROP TABLE IF EXISTS transaction";


    private static DBHandler instance;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Singleton */
    public static DBHandler getHelper(Context context){
        if (instance == null)
            instance = new DBHandler(context);
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ACCOUNT_ENTRIES);
        db.execSQL(DELETE_TRANSACTION_ENTRIES);
        onCreate(db);
    }


}


