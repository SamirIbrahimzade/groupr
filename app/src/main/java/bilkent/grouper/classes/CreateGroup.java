package bilkent.grouper.classes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupr.groupr.R;

public class CreateGroup extends AppCompatActivity {

    EditText crGroup;
    EditText p1;
    EditText p2;
    Button submit;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        crGroup = findViewById(R.id.createGroupName);
        p1 = findViewById(R.id.createGroupPassword);
        p2 = findViewById(R.id.createGroupPassword2);
        submit = findViewById(R.id.createGroupButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                createGroup(user,crGroup.getText().toString(),p1.getText().toString(),p2.getText().toString());
            }
        });

    }

    public void createGroup(FirebaseUser user, String name,String password,String passwordCheck){
        if(password.equals(passwordCheck)){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.setValue(name);
        }
        else{
            Toast.makeText(CreateGroup.this,"Passwords are different", Toast.LENGTH_LONG).show();
        }

    }

}


