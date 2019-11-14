package Pro.Wash;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private Context context;
    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {

        this.context = context;
        getSharedPreference();
    }
    private void getSharedPreference(){
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.mypreferences),Context.MODE_PRIVATE);
    }
    public void writePreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.mypreferencekey),"OK");
        editor.commit();
    }
    public boolean CheckPreference(){
        boolean status = false;
        status = !sharedPreferences.getString(context.getString(R.string.mypreferencekey), "null").equals("null");
        return status;
    }
    public void ClearPreference(){
        sharedPreferences.edit().clear().commit();
    }
}
