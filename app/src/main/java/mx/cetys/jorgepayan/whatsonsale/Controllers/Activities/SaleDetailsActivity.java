package mx.cetys.jorgepayan.whatsonsale.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import mx.cetys.jorgepayan.whatsonsale.R;
import mx.cetys.jorgepayan.whatsonsale.Utils.DB.Helpers.CategoryHelper;
import mx.cetys.jorgepayan.whatsonsale.Utils.DB.Helpers.SaleHelper;
import mx.cetys.jorgepayan.whatsonsale.Utils.SimpleDialog;
import mx.cetys.jorgepayan.whatsonsale.Utils.Utils;

public class SaleDetailsActivity extends AppCompatActivity {

    EditText editTextSaleDescription;
    EditText editTextSaleExpirationDate;
    Spinner spinnerCategory;
    Button btnAddSale;

    String saleDescription;
    String saleExpirationDate;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_sale_details);
        final FragmentManager fm = getSupportFragmentManager();

        final SaleHelper saleHelper = new SaleHelper(getApplicationContext());
        final CategoryHelper categoryHelper = new CategoryHelper(getApplicationContext());
        editTextSaleDescription = (EditText)findViewById(R.id.edit_text_saleDescription);
        editTextSaleExpirationDate = (EditText)findViewById(R.id.edit_text_expirationDate);
        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        btnAddSale = (Button)findViewById(R.id.btn_addSale);

        editTextSaleExpirationDate.setText(
            new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, categoryHelper.getAllCategories());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(adapter);

        btnAddSale.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleDescription = editTextSaleDescription.getText().toString();
                saleExpirationDate = editTextSaleExpirationDate.getText().toString();
                category = spinnerCategory.getSelectedItem().toString();

                if(saleDescription.length() > 0 && saleExpirationDate.length() > 0){

                    final String saleId = saleHelper.addSale("",
                        BusinessHomeActivity.currentBusiness.getBusinessId(),
                        category, saleDescription, saleExpirationDate);

                    Utils.post("sale", getApplicationContext(), new HashMap<String, String>(){{
                        put("business_id", BusinessHomeActivity.currentBusiness.getBusinessId());
                        put("category_name", category); put("description", saleDescription);
                        put("expiration_date", saleExpirationDate); put("sale_id", saleId);
                    }});

                    Intent data = new Intent();
                    data.putExtra( "SUCCESS","Success." );
                    setResult(RESULT_OK,data);
                    finish();
                } else {
                    SimpleDialog emptyFieldsDialog =
                            new SimpleDialog("Fill up all the fields before adding sale.", "Ok");
                    emptyFieldsDialog.show(fm, "Alert Dialog Fragment");
                }
            }
        } );

        editTextSaleExpirationDate.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar mcurrentDate=Calendar.getInstance();
            int mYear=mcurrentDate.get(Calendar.YEAR);
            int mMonth=mcurrentDate.get(Calendar.MONTH);
            int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker=new DatePickerDialog(SaleDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth,
                                      int selectedDay) {
                    editTextSaleExpirationDate.setText(selectedmonth+1 + "/" + selectedDay + "/" +
                        selectedyear );
                }
            },mYear, mMonth, mDay);
            mDatePicker.setTitle("Select expiration date");
            mDatePicker.show();  }
    } );
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}