package ovh.thesoulforge.guildchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Michal on 2015-10-11.
 */
public class PreferecesLoader {
    private void loadSavedPreferences(EditText txtLogin, EditText txtHaslo, CheckBox checkBox, Context con) {
        String login = txtLogin.getText().toString();
        String haslo = txtHaslo.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
        String name = sharedPreferences.getString("Login", login);
        String pass = sharedPreferences.getString("Haslo", haslo);
        if (checkBoxValue) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        txtLogin.setText(name);
        txtHaslo.setText(pass);
    }

    
}
