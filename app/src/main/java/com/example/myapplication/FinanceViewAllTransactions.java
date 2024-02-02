package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FinanceViewAllTransactions extends AppCompatActivity implements FinanceTransactionDialog.FinanceTransactionDialogListener, View.OnClickListener{

    private DBHandler db;
    private ArrayList<FinanceTransaction> mTransactions = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_view_all_transactions);
        db = new DBHandler(this);

        // Initialize DBHandler
        mTransactions = db.getAllFinanceTransactions();
        setUIRef();

        EditText editTextSearch = findViewById(R.id.searchEditText);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterTransactions(s.toString());
            }
        });
    }

    private void filterTransactions(String searchText) {
        // Use DBHandler to search transactions based on searchText
        List<FinanceTransaction> filteredList = db.searchFinanceTransactions(searchText);

        // Update the adapter with the filtered list
        ((FinanceTransactionAdapter) mRecyclerView.getAdapter()).updateData(new ArrayList<>(filteredList));
    }


    private void setUIRef() {
        mRecyclerView = findViewById(R.id.recentRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        FinanceTransactionAdapter myAdapter = new FinanceTransactionAdapter(mTransactions, transaction -> {
            // Recycler item click opens the dialog for editing
            showFinanceTransactionDialog(transaction, false); // 'false' for edit
        });
        mRecyclerView.setAdapter(myAdapter);
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, FinanceTransaction transaction, boolean isNew) {
        if (isNew) {
            // Add the new transaction to the database
            db.addFinanceTransaction(transaction);
        } else {
            // Update the existing transaction in the database
            db.updateFinanceTransaction(transaction);
        }
        // Refresh the UI to reflect changes
        refreshTransactions();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Handle the case where the user cancels the dialog
    }

    public void showFinanceTransactionDialog(FinanceTransaction transaction, boolean isNew) {
        FinanceTransactionDialog dialogFragment = FinanceTransactionDialog.newInstance(transaction, isNew);
        dialogFragment.show(getSupportFragmentManager(), "FinanceTransactionDialog");
    }

    @Override
    public void onDialogDeleteClick(DialogFragment dialog, FinanceTransaction transaction) {
        if (transaction != null) {
            // Perform the delete operation
            db.deleteFinanceTransaction(transaction.getId());

            // Refresh the UI (e.g., by reloading the data for the RecyclerView)
            refreshTransactions();

            // Optionally, show a confirmation message
            Toast.makeText(this, "Transaction deleted", Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshTransactions() {
        // Reload transactions from the database and update the RecyclerView
        mTransactions.clear();
        mTransactions.addAll(db.getAllFinanceTransactions());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_button) {

        } else if (view.getId() == R.id.imageButton) {
            // Plus icon clicked, open dialog for adding a new transaction
            showFinanceTransactionDialog(new FinanceTransaction(), true); // 'true' for new transaction
        } else if (view.getId() == R.id.searchTransaction) {
            Intent i = new Intent(this, FinanceViewAllTransactions.class);
            startActivity(i);
        }
    }
}
