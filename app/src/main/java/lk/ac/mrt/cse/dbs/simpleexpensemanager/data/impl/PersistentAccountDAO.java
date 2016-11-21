package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


/**
 * Created by ASUS on 2016-11-20.
 */
public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    public PersistentAccountDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override

    public List<String> getAccountNumbersList() {

        List<String> acc_no = new ArrayList<>();
        Cursor cursor = db.query("account", new String[]{"account_no"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                acc_no.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return acc_no;
    }

    @Override
    public List<Account> getAccountsList() {

        List<Account> acc = new ArrayList<>();
        Cursor cursor = db.query("account", new String[]{"account_no", "bank", "acc_holder", "balance"}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                acc.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return acc;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Account acc;
        Cursor cursor = db.query("account", new String[]{"account_no", "bank", "acc_holder", "balance"}, "account_no =?", new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }
        acc = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));

        cursor.close();
        return acc;
    }

    @Override
    public void addAccount(Account account) {


        ContentValues values = new ContentValues();
        values.put("account_no", account.getAccountNo());
        values.put("bank", account.getBankName());
        values.put("acc_holder", account.getAccountHolderName());
        values.put("balance", account.getBalance());
        // Inserting Row
        db.insert("account", null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        db.delete("account", "account_no = ?",
                new String[]{String.valueOf(accountNo)});

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Cursor cursor = db.query("account", new String[]{"account_no", "bank", "acc_holder", "balance"}, "account_no =?", new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

        }

        double balance = cursor.getDouble(3);

        ContentValues values = new ContentValues();
        if (expenseType == ExpenseType.EXPENSE) {
            values.put("balance", balance - amount);
        } else {
            values.put("balance", balance + amount);
        }

        cursor.close();
    }
}
