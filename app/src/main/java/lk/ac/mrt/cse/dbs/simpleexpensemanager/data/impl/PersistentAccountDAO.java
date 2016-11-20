package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DBHandler;

/**
 * Created by ASUS on 2016-11-20.
 */
public class PersistentAccountDAO implements AccountDAO {
    private final DBHandler dbHandler;

    public PersistentAccountDAO(Context context) {
        dbHandler = DBHandler.getHelper(context);
    }

    @Override

    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<String> acc_no = new ArrayList<>();
        Cursor cursor = db.query("account", new String[]{"account_no"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                acc_no.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        db.close();
        return acc_no;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Account> acc = new ArrayList<>();
        Cursor cursor = db.query("account", new String[]{"account_no", "bank", "acc_holder", "balance"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                acc.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }

        db.close();
        return acc;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Account acc;
        Cursor cursor = db.query("account", new String[]{"account_no", "bank", "acc_holder", "balance"}, "account_no =?", new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));

        db.close();
        return acc;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("account_no", account.getAccountNo());
        values.put("bank", account.getBankName());
        values.put("acc_holder", account.getAccountHolderName());
        values.put("balance", account.getBalance());
        // Inserting Row
        db.insert("account", null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        db.delete("account", "account_no = ?",
                new String[]{String.valueOf(accountNo)});
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();


        String sql = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();


    }
}
