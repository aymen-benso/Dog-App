package com.example.doggoo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class payActivity extends AppCompatActivity {

    private EditText cardNumber, cardExpiry, cardCVC;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        cardNumber = findViewById(R.id.card_number);
        cardExpiry = findViewById(R.id.card_expiry);
        cardCVC = findViewById(R.id.card_cvc);
        payButton = findViewById(R.id.pay_button);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        // Example validation; implement actual payment processing here
        String number = cardNumber.getText().toString();
        String expiry = cardExpiry.getText().toString();
        String cvc = cardCVC.getText().toString();

        if (number.isEmpty() || expiry.isEmpty() || cvc.isEmpty()) {
            Toast.makeText(payActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Process the payment here
            Toast.makeText(payActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to previous screen
        }
    }
}