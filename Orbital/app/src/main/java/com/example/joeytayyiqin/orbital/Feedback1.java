package com.example.joeytayyiqin.orbital;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class Feedback1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback1);
    }

    public void send_click(View v)
    {
        EditText Name = (EditText) findViewById(R.id.Name);
        EditText Email = (EditText) findViewById(R.id.Email);
        EditText Feedback = (EditText) findViewById(R.id.Feedback);

        if(Name.getText().toString().equals(""))
            Name.setError("Mandatory field");
        else if(Email.getText().toString().equals(""))
            Email.setError("Mandatory field");
        else if(Feedback.getText().toString().equals(""))
            Feedback.setError("Mandatory field");
        else
        {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_EMAIL, new String[] {Email.getText().toString()});
//            i.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
            i.putExtra(Intent.EXTRA_TEXT, "To the Committee Head, \n" + Feedback.getText().toString() +"\n Thank you, "
                    + Name.getText().toString());

            try {
                startActivity(Intent.createChooser(i, "Send Mail"));
            }catch (android.content.ActivityNotFoundException ex)
            {
                Toast.makeText(Feedback1.this, "No Mail App Found", Toast.LENGTH_SHORT).show();
            }catch (Exception ex)
            {
                Toast.makeText(Feedback1.this, "Unexpected Error" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
