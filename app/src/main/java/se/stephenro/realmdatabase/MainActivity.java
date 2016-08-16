package se.stephenro.realmdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Realm realm;
    private RealmConfiguration realmConfig;

    @Bind(R.id.helloWorldTextView)
    TextView helloWorld;

    @Bind(R.id.saveButton)
    Button saveButton;

    @Bind(R.id.resetButton)
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        helloWorld.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        // Start Realm
        realmConfig = new RealmConfiguration.Builder(this).build();

        // If you open this here it will be opened on the UI thread. Sooo simple
        realm = Realm.getInstance(realmConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helloWorldTextView:
                save();
                break;

            case R.id.resetButton:
                Log.d(TAG, "Deleting");

                realm.beginTransaction();
                realm.allObjects(Person.class).clear();
                realm.commitTransaction();

                refresh(realm);
                break;

            case R.id.saveButton:
                save();
                break;
        }
    }

    private void save() {
        Log.d(TAG, "Saving");

        realm.beginTransaction();

        Person person = realm.createObject(Person.class);
        person.setFirstName("Stephen");
        person.setLastName("Rose");
        person.setAge(Calendar.getInstance().get(Calendar.SECOND));

        realm.commitTransaction();

        refresh(realm);
    }

    private void refresh(Realm realm) {
        // Refresh List
        RealmResults<Person> resultSet = realm.allObjects(Person.class);
        String answer = "";
        Log.d(TAG, "Refreshing");

        for (Person person : resultSet) {
            answer = answer
                    + person.getFirstName()
                    + " "
                    + person.getLastName()
                    + "\n"
                    + person.getAge()
                    + "\n";

        }

        helloWorld.setText(answer);
    }
}
