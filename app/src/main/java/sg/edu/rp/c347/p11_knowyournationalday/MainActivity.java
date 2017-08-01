package sg.edu.rp.c347.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvFacts;
    ArrayList<String> alFacts = new ArrayList<>();
    ArrayAdapter<String> aaFacts;
    String accessCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFacts = (ListView) findViewById(R.id.listViewFacts);
        aaFacts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alFacts);

        alFacts.add("There are total of 170 train stations in Singapore ");
        alFacts.add("There are 5 stars on Singapore Flag");
        alFacts.add("Singapore is famous for its cleanilness");


        lvFacts.setAdapter(aaFacts);
    }

    protected void onStart(){
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String code = prefs.getString("accessCode", "");
        accessCode = code;

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase
                .findViewById(R.id.editTextPassPhrase);

        if(accessCode.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(passPhrase)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String passcode = "738964";
                            if (etPassphrase.getText().toString().equals(passcode)) {
                                accessCode = etPassphrase.getText().toString();
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Access Code", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("No access code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "This app requires an access code", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int count = alFacts.size();
        String message = "";
        for (int x = 0; x < count; x++) {
            String content = alFacts.get(x);
            message += content;
        }

        if (item.getItemId() == R.id.sendToFriend) {
            String[] options = new String[]{"Email", "SMS"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String finalMessage = message;
            builder.setTitle("Select the way to enrich your friend")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_TEXT, finalMessage);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                            } else {
                                Intent smsIntent = new Intent(Intent.ACTION_SEND);
                                smsIntent.setType("text/plain");
                                smsIntent.putExtra(Intent.EXTRA_TEXT, alFacts.get(0) + "\n" + alFacts.get(1) + "\n" + alFacts.get(2));
                                try {
                                    startActivity(Intent.createChooser(smsIntent, "Send sms..."));
                                    finish();
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(passPhrase)
                    .setNegativeButton("Don't Know Lah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            RadioGroup rg1 = (RadioGroup)passPhrase.findViewById(R.id.rg1);
                            RadioGroup rg2 = (RadioGroup)passPhrase.findViewById(R.id.rg2);
                            RadioGroup rg3 = (RadioGroup)passPhrase.findViewById(R.id.rg3);

                            int first = rg1.getCheckedRadioButtonId();
                            int second = rg2.getCheckedRadioButtonId();
                            int third = rg3.getCheckedRadioButtonId();

                            RadioButton rb1 = (RadioButton)passPhrase.findViewById(first);
                            RadioButton rb2 = (RadioButton)passPhrase.findViewById(second);
                            RadioButton rb3 = (RadioButton)passPhrase.findViewById(third);
                            int score = 0;
                            if (rb1.getText().toString().equalsIgnoreCase("No")) {
                                score += 1;
                            } else {
                            }
                            if (rb2.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            } else {
                            }
                            if (rb3.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            } else {
                            }
                            Toast.makeText(MainActivity.this, "Score " + score,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);

        } else if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop(){
        super.onStop();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("accessCode", accessCode);
        prefEdit.commit();
    }


}
