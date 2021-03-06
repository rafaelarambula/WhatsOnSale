package mx.cetys.jorgepayan.whatsonsale.Utils.DB.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import mx.cetys.jorgepayan.whatsonsale.Models.Customer;
import mx.cetys.jorgepayan.whatsonsale.Utils.DB.DBUtils;
import mx.cetys.jorgepayan.whatsonsale.Utils.Utils;

/**
 * Created by jorge.payan on 11/17/17.
 */

public class CustomerHelper {
    private DBUtils dbHelper;
    private SQLiteDatabase database;

    private String[] CUSTOMER_TABLE_COLUMNS = {
            DBUtils.CUSTOMER_ID,
            DBUtils.CUSTOMER_USER_EMAIL,
            DBUtils.CUSTOMER_NAME,
            DBUtils.CUSTOMER_AGE,
            DBUtils.CUSTOMER_GENDER
    };

    public CustomerHelper(Context context) {
        dbHelper = new DBUtils(context);
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public Customer getCustomer(String customerId) {
        open();
        Cursor cursor = database.query(DBUtils.CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_COLUMNS,
                DBUtils.CUSTOMER_ID + " = '" + customerId+"'", null, null, null, null);

        cursor.moveToFirst();
        Customer customer = parsecustomer(cursor);
        cursor.close();
        close();

        return customer;
    }

    public Customer getCustomerByEmail(String customerEmail) {
        open();
        Cursor cursor = database.query(DBUtils.CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_COLUMNS,
                DBUtils.CUSTOMER_USER_EMAIL + " = '" + customerEmail+  "'", null, null, null, null);

        cursor.moveToFirst();
        Customer customer = parseCustomer(cursor);
        cursor.close();
        close();

        return customer;
    }

    public Customer getCustomerByEmail(String userEmail) {
        open();
        Cursor cursor = database.query(DBUtils.CUSTOMER_TABLE_NAME, CUSTOMER_TABLE_COLUMNS,
                DBUtils.CUSTOMER_USER_EMAIL + " = '" + userEmail + "'", null, null, null, null);

        cursor.moveToFirst();
        Customer customer = parseCustomer(cursor);
        cursor.close();
        close();
        return customer;
    }

    public String addCustomer(String customerId, String userEmail, String name, int age, String gender) {
        open();
        ContentValues values = new ContentValues();

        customerId = (customerId.isEmpty()) ? Utils.generateId() : customerId;

        values.put(DBUtils.CUSTOMER_ID, customerId);
        values.put(DBUtils.CUSTOMER_NAME, name);
        values.put(DBUtils.CUSTOMER_AGE, age);
        values.put(DBUtils.CUSTOMER_GENDER, gender);
        values.put(DBUtils.CUSTOMER_USER_EMAIL, userEmail);

        database.insert(DBUtils.CUSTOMER_TABLE_NAME, null, values);
        close();

        return customerId;
    }

    public int updateCustomer(Customer customer) {
        open();
        ContentValues values = new ContentValues();

        values.put(DBUtils.CUSTOMER_ID, customer.getCustomerId());
        values.put(DBUtils.CUSTOMER_USER_EMAIL, customer.getUserEmail());
        values.put(DBUtils.CUSTOMER_NAME, customer.getName());
        values.put(DBUtils.CUSTOMER_AGE, customer.getAge());
        values.put(DBUtils.CUSTOMER_GENDER, customer.getGender());

        int response = database.update(DBUtils.CUSTOMER_TABLE_NAME, values, DBUtils.CUSTOMER_ID + " = '" + customer.getCustomerId() + "'",
                null);
        close();
        return response;
    }

    public void deleteCustomer(String customerId) {
        open();
        database.delete(DBUtils.CUSTOMER_TABLE_NAME, DBUtils.CUSTOMER_ID + " = " + customerId, null);
        close();
    }

    public void clearTable() {
        open();
        database.execSQL("DELETE FROM " + DBUtils.CUSTOMER_TABLE_NAME);
        close();
    }

    private Customer parseCustomer(Cursor cursor) {
        String customerId = cursor.getString(cursor.getColumnIndex(DBUtils.CUSTOMER_ID));
        String userEmail = cursor.getString(cursor.getColumnIndex(DBUtils.CUSTOMER_USER_EMAIL));
        String name = cursor.getString(cursor.getColumnIndex(DBUtils.CUSTOMER_NAME));
        int age = cursor.getInt(cursor.getColumnIndex(DBUtils.CUSTOMER_AGE));
        String gender = cursor.getString(cursor.getColumnIndex(DBUtils.CUSTOMER_GENDER));

        return new Customer(customerId, userEmail, name, age, gender);
    }
}
