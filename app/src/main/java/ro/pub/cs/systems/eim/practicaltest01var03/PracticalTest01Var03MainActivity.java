package ro.pub.cs.systems.eim.practicaltest01var03;

import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.EQUATION1;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.EQUATION2;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.SERVICE_STARTED;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.SERVICE_STOPPED;
import static ro.pub.cs.systems.eim.practicaltest01var03.ProcessingThread.actionTypes;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PracticalTest01Var03MainActivity extends AppCompatActivity {

    private EditText editText1, editText2;
    private Button buttonPlus, buttonMinus, transferButton;
    private TextView resultTextView;
    int num1,num2, result;
    String operationSymbol;
    private int serviceStatus = SERVICE_STOPPED;
    private final IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var03_main);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);
        transferButton = findViewById(R.id.transferButton);
        resultTextView = findViewById(R.id.resultTextView);

        for (int index = 0; index < actionTypes.length; index++) {
            intentFilter.addAction(actionTypes[index]);
        }

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('+');
                if (serviceStatus == SERVICE_STOPPED) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var03Service.class);

                    intent.putExtra(EQUATION1, num1 + " " + operationSymbol + " " + num2 + " = " + result);
                    intent.putExtra(EQUATION2, num1 + " " + "-" + " " + num2 + " = " + result);
                    getApplicationContext().startService(intent);
                    serviceStatus = SERVICE_STARTED;
                }
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOperation('-');
                if (serviceStatus == SERVICE_STOPPED) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var03Service.class);

                    intent.putExtra(EQUATION1, num1 + " " + operationSymbol + " " + num2 + " = " + result);
                    intent.putExtra(EQUATION2, num1 + " " + "+" + " " + num2 + " = " + result);
                    getApplicationContext().startService(intent);
                    serviceStatus = SERVICE_STARTED;
                }
            }
        });

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Var03SecondActivity.class);
                intent.putExtra("result", resultTextView.getText().toString());
                startActivityForResult(intent, 1); // Porniți activitatea secundară pentru a aștepta un rezultat înapoi
            }
        });
    }

    private void performOperation(char operation) {
        if (!isValidInt(editText1.getText().toString()) || !isValidInt(editText2.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Introduceți două numere întregi valide", Toast.LENGTH_SHORT).show();
            return;
        }

         num1 = Integer.parseInt(editText1.getText().toString());
         num2 = Integer.parseInt(editText2.getText().toString());
         result = 0;
         operationSymbol = "";

        if (operation == '+') {
            result = num1 + num2;
            operationSymbol = "+";
        } else if (operation == '-') {
            result = num1 - num2;
            operationSymbol = "-";
        }

        resultTextView.setText(num1 + " " + operationSymbol + " " + num2 + " = " + result);
    }

    private boolean isValidInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editText1", editText1.getText().toString());
        outState.putString("editText2", editText2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("editText1")) {
            String savedStateEditText1 = savedInstanceState.getString("editText1");
//            editText1.setText(savedStateEditText1);
            Toast.makeText(getApplicationContext(), "Valoarea 1: " + savedStateEditText1, Toast.LENGTH_SHORT).show();
        }
        if (savedInstanceState.containsKey("editText2")) {
            String savedStateEditText2 = savedInstanceState.getString("editText2");
//            editText2.setText(savedStateEditText2);
            Toast.makeText(getApplicationContext(), "Valoarea 2: " + savedStateEditText2, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromSecondaryActivity");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Actiune anulata", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Var03Service.class);
        stopService(intent);
        super.onDestroy();
    }
}