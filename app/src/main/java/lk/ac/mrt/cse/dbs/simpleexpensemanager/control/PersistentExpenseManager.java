package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DBHandler;

/**
 * Created by ASUS on 2016-11-20.
 */
public class PersistentExpenseManager extends ExpenseManager{
    private Context context;

    public PersistentExpenseManager(Context context) {
        this.context=context;
        setup();
    }


    @Override
    public void setup(){

        SQLiteDatabase mydatabase = context.openOrCreateDatabase("140340E", context.MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS account(" +
                "account_no VARCHAR PRIMARY KEY," +
                "bank VARCHAR," +
                "acc_holder VARCHAR," +
                "balance REAL" +
                " );");


        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS transactionLogger(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "account_no VARCHAR," +
                "expense_type INT," +
                "amount REAL," +
                "transaction_date DATE," +
                "FOREIGN KEY (account_no) REFERENCES account(account_no)" +
                ");");



        //These two functions will hold our DAO instances in memory till the program exists
        PersistentAccountDAO accountDAO = new PersistentAccountDAO(mydatabase);
        //accountDAO.addAccount(new Account("Account12","Sampath bank","Manujith",500));

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(mydatabase));
    }
}
