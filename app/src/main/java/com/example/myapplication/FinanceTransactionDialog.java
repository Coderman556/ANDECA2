package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class FinanceTransactionDialog extends DialogFragment {

    public interface FinanceTransactionDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, FinanceTransaction transaction, boolean isNew);
        void onDialogNegativeClick(DialogFragment dialog);
        void onDialogDeleteClick(DialogFragment dialog, FinanceTransaction transaction);
    }

    FinanceTransactionDialogListener listener;
    private FinanceTransaction transaction;
    private boolean isNew;

    // Static factory method to create a new instance of the dialog
    // isNew indicates whether this is for a new transaction or editing an existing one
    public static FinanceTransactionDialog newInstance(FinanceTransaction transaction, boolean isNew) {
        FinanceTransactionDialog fragment = new FinanceTransactionDialog();
        Bundle args = new Bundle();
        args.putParcelable("transaction", transaction); // Assuming FinanceTransaction implements Parcelable
        args.putBoolean("isNew", isNew);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Assuming FinanceTransaction implements Parcelable
            transaction = getArguments().getParcelable("transaction");
            isNew = getArguments().getBoolean("isNew");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Inflate and set the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.finance_custom_layout_for_insertdata, null);

        // Extract the transaction data from arguments
        Bundle args = getArguments();
        FinanceTransaction transaction = args.getParcelable("transaction");
        boolean isNew = args.getBoolean("isNew");

        if (!isNew && transaction != null) {
            // Use transaction data to populate fields
            EditText amountEditText = view.findViewById(R.id.amount_edt); // Assuming you have an EditText for amount
            EditText categoryEditText = view.findViewById(R.id.cat_edt); // And one for category
            EditText descriptionEditText = view.findViewById(R.id.description); // And one for category
            // Populate fields with transaction data
            amountEditText.setText(String.valueOf(transaction.getPrice()));
            categoryEditText.setText(transaction.getCategory());
            descriptionEditText.setText(transaction.getDescription());
            // Do the same for other fields
        }

        // Only add the "Delete" button for existing transactions
        if (!isNew && transaction != null) {
            builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle delete action
                    listener.onDialogDeleteClick(FinanceTransactionDialog.this, transaction);
                }
            });
        }
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText amountEditText = view.findViewById(R.id.amount_edt);
                        EditText categoryEditText = view.findViewById(R.id.cat_edt);
                        EditText descriptionEditText = view.findViewById(R.id.description);

                        // Convert amount to double, handle possible NumberFormatException
                        double amount;
                        try {
                            amount = Double.parseDouble(amountEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            // Handle exception, perhaps show a toast to the user
                            Toast.makeText(getContext(), "Invalid amount", Toast.LENGTH_SHORT).show();
                            return; // Don't dismiss the dialog, amount is invalid
                        }

                        final FinanceTransaction[] transactionContainer = new FinanceTransaction[1];

                        // Check if the transaction is initially null
                        if (transaction == null) {
                            transactionContainer[0] = new FinanceTransaction(); // Initialize it if null
                        } else {
                            transactionContainer[0] = transaction; // Use the existing transaction
                        }

                        transactionContainer[0].setCategory(categoryEditText.getText().toString());
                        transactionContainer[0].setDescription(descriptionEditText.getText().toString());
                        transactionContainer[0].setPrice(amount);

                        // Invoke the listener passing the new or updated transaction
                        listener.onDialogPositiveClick(FinanceTransactionDialog.this, transactionContainer[0], isNew);
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FinanceTransactionDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    // Override the Fragment.onAttach() method to instantiate the FinanceTransactionDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the FinanceTransactionDialogListener so we can send events to the host
            listener = (FinanceTransactionDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw an exception
            throw new ClassCastException(context.toString() + " must implement FinanceTransactionDialogListener");
        }
    }
}
