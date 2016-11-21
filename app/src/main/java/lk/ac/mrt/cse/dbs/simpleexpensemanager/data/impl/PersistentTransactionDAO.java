package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
//import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DBHandler;

/**
 * Created by ASUS on 2016-11-20.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteDatabase db){
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {



        ContentValues values = new ContentValues();
        values.put("transaction_date", date.getTime());
        values.put("account_no", accountNo);
        values.put("expense_type",(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        values.put("amount", amount);
        // Inserting Row
        db.insert("transactionLogger", null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        String select_Query = "SELECT * from transactionLogger";

        Cursor cursor = db.rawQuery(select_Query, null);
        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(0));
                String acc_no = cursor.getString(1);

                ExpenseType expenseType = ExpenseType.INCOME;
                if (cursor.getInt(2) == 1) {
                    expenseType = ExpenseType.EXPENSE;
                }

                double amount = cursor.getDouble(3);

                Transaction transaction = new Transaction(date, acc_no, expenseType, amount);
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();


        Cursor cursor = db.rawQuery("SELECT * FROM transactionLogger LIMIT " + limit,null);

        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(0));
                String acc_no = cursor.getString(1);

                ExpenseType expenseType = ExpenseType.INCOME;
                if (cursor.getInt(2) == 1) {
                    expenseType = ExpenseType.EXPENSE;
                }

                double amount = cursor.getDouble(3);

                Transaction transaction = new Transaction(date, acc_no, expenseType, amount);
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }

        return transactions;
    }
}
