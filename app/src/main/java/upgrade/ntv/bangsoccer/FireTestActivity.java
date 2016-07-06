package upgrade.ntv.bangsoccer;



import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class FireTestActivity extends AppCompatActivity {

    TextView vCondition;
    Button vButtonSunny, vButtonFoggy;

    DatabaseReference mFireBaseRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFireBaseConditionRef = mFireBaseRootRef.child("condition");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_test);


        vCondition = (TextView) findViewById(R.id.conditionText);
        vButtonSunny = (Button) findViewById(R.id.sunny);
        vButtonFoggy= (Button) findViewById(R.id.foggy);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFireBaseConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String condition = dataSnapshot.getValue(String.class);
                vCondition.setText(condition);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        vButtonSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFireBaseConditionRef.setValue("Sunny");
            }
        });

        vButtonFoggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFireBaseConditionRef.setValue("Foggy");
            }
        });

    }
}