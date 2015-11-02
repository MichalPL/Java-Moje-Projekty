package ovh.thesoulforge.guildchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private Socket socket;
    private static final int SERVERPORT = 9999;
    private static final String SERVER_IP = "136.243.44.221";//"10.0.2.2";//
    private static final double VER = 2104;
    private String nick;
    AlertDialog alertDialog = null;
    MessagesListAdapter adapter;
    List<Message> listMessages;
    ListView listViewMessages;
    CheckBox checkBox;
    EditText txtLogin;
    EditText txtHaslo;
    RelativeLayout layout1;
    RelativeLayout layout2;
    EditText txtWyslij;
    Button btnZaloguj;
    Button btnWyslij;
    TextView lblLogowanie;
    EditText txtDo;
    Button btnZnajomi;
    TextView lblLog;

    int ukryj = 0;
    boolean zminimalizowane = false;
    int ID_NOT = 1234;
    boolean zalog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new ClientThread()).start();
        //NOWE
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listMessages = new ArrayList<Message>();

        adapter = //new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listMessages);
                new MessagesListAdapter(getApplicationContext(), listMessages);
        listViewMessages.setAdapter(adapter);
        alertDialog = new AlertDialog.Builder(this).create();
        checkBox = (CheckBox)findViewById(R.id.checkBox1);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtHaslo = (EditText) findViewById(R.id.txtHaslo);
        layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        txtWyslij = (EditText) findViewById(R.id.txtWyslij);
        btnZaloguj = (Button) findViewById(R.id.btnZaloguj);
        btnWyslij = (Button) findViewById(R.id.btnWyslij);
        lblLogowanie = (TextView) findViewById(R.id.textView2);
        txtDo = (EditText) findViewById(R.id.txtDo);
        btnZnajomi = (Button) findViewById(R.id.btnZnajomi);
        lblLog = (TextView) findViewById(R.id.log);
        loadSavedPreferences();

        //NOWE
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        zminimalizowane = true;
    }


    @Override
     public void onResume() {
        super.onResume();  // Always call the superclass method first
        zminimalizowane = false;
        if (zalog) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(ID_NOT);
            if (ukryj != 0) {
                ukryjLayout1();
            } else {
                ukryj = 1;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void notka(String tytul, String tresc){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle(tytul)
                        .setContentText(tresc);
        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Notification.FLAG_AUTO_CANCEL);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mId = ID_NOT;
        mNotificationManager.notify(mId, mBuilder.build());
    }

    private void loadSavedPreferences() {
        String login = txtLogin.getText().toString();
        String haslo = txtHaslo.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
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

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void ukryjLayout1()
    {
        layout2.setVisibility(RelativeLayout.INVISIBLE);
        txtLogin.setVisibility(EditText.INVISIBLE);
        txtHaslo.setVisibility(EditText.INVISIBLE);
        btnZaloguj.setVisibility(Button.INVISIBLE);
        lblLogowanie.setVisibility(TextView.INVISIBLE);

        layout1.setVisibility(RelativeLayout.VISIBLE);
        listViewMessages.setVisibility(ListView.VISIBLE);
        txtWyslij.setVisibility(EditText.VISIBLE);
        btnWyslij.setVisibility(Button.VISIBLE);
        txtDo.setVisibility(Button.VISIBLE);
        btnZnajomi.setVisibility(Button.VISIBLE);
        txtWyslij.requestFocus();
    }

    public void onClickZnajomi(View view) {
        try {

            JSONObject json = new JSONObject();
            json.put("typ", "4");
            json.put("text1", "Client");
            json.put("text2", "Zadanie o znajomych");
            String jsonStr = json.toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(jsonStr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickLoguj(View view) {
        EditText txtLogin = (EditText) findViewById(R.id.txtLogin);
        EditText txtHaslo = (EditText) findViewById(R.id.txtHaslo);
        String login = txtLogin.getText().toString();
        String haslo = txtHaslo.getText().toString();
        try {

            JSONObject json = new JSONObject();
            json.put("typ", "1");
            json.put("text1", login);
            json.put("text2", haslo);
            String jsonStr = json.toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(jsonStr);
            nick = login;
            savePreferences("CheckBox_Value", checkBox.isChecked());
            if (checkBox.isChecked()) {
                savePreferences("Login", login);
                savePreferences("Haslo", haslo);
            }
            else {
                savePreferences("Login", "");
                savePreferences("Haslo", "");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onClickWyslij(View view) {
        EditText txtWyslij = (EditText) findViewById(R.id.txtWyslij);
        EditText txtDo = (EditText) findViewById(R.id.txtDo);
        String wiadomosc = txtWyslij.getText().toString();
        String doKogo = txtDo.getText().toString();
        try {
            if (wiadomosc != null && !wiadomosc.isEmpty() && doKogo != null && !doKogo.isEmpty() && !doKogo.equals(nick)) {
                JSONObject json = new JSONObject();
                json.put("typ", "2");
                json.put("text1", doKogo);
                json.put("text2", wiadomosc);
                String jsonStr = json.toString();
                PrintWriter out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println(jsonStr);
                listMessages.add(new Message(nick, wiadomosc, 1));
                adapter.notifyDataSetChanged();
                txtWyslij.setText("");
            }
            else
            {
                TextView lblLog = (TextView) findViewById(R.id.log);
                lblLog.setText("Nie możesz wysłać pustej wiadomości lub do siebie!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class ClientThread implements Runnable {
        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

                InputStream stm = socket.getInputStream();
                savePreferences("zalogowany", false);
                byte[] bb = new byte[2048];
                int k;
                while (socket.isConnected()) {
                    k = stm.read(bb, 0, 2048);
                    char[] calosc = new char[k];
                    for (int i = 0; i < k; i++)
                    {
                        calosc[i] = (char)bb[i];
                    }
                    final String data = new String(calosc);


                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String,Object>>(){}.getType();
                    final Map<String, Object> wynik = gson.fromJson(data.trim(), type);
                    final String typ = wynik.get("typ").toString();

                    switch (typ)
                    {
                        case "serv1":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Połączono!");

                                    if((double)wynik.get("text2") != VER)
                                    {
                                        alertDialog.setTitle("Aktualizacja");
                                        alertDialog.setMessage("Wymagana aktualizacja aplikacji!");
                                        alertDialog.show();
                                    }
                                }
                            });
                            break;
                        case "l1":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Zalogowano!");
                                    savePreferences("zalogowany", true);
                                    ukryjLayout1();
                                    wczytajWiadomosci();
                                }
                            });
                            break;
                        case "l2":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Ktoś jest zalogowany!");
                                }
                            });
                            break;
                        case "l3":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Złe dane...");
                                }
                            });
                            break;
                        case "l4":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Jesteś już zalogowany...");
                                }
                            });
                            break;
                        case "info1":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Brak użytkownika!");
                                    listMessages.add(new Message("Błąd...", "Wiadomość nie została wysłana", 2));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case "info2":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Wiadomość wysłana!");
                                }
                            });
                            break;
                        case "info3a":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listMessages.add(new Message("Nowe wiadomości", "zostaną automatycznie usunięte z bazy danych", 2));
                                    adapter.notifyDataSetChanged();
                                    czyOnline();
                                }
                            });
                            break;
                        case "info3b":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lblLog.setText("Brak wiadomości!");
                                    czyOnline();
                                }
                            });
                            break;

                        case "wiad":
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    listMessages.add(new Message(wynik.get("text1").toString(), wynik.get("text2").toString(), 3));
                                    adapter.notifyDataSetChanged();
                                    if(zminimalizowane){
                                        notka("Nowa wiadomość", "Otrzymałeś nową wiadomość!");
                                    }
                                    uzupelnijDo(wynik.get("text1").toString());
                                }

                            });
                            break;
                        case "wiad2":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    int ilosc = 0;
                                    ArrayList<Object> list1 = (ArrayList<Object>) wynik.get("text1");
                                    ArrayList<Object> list2 = (ArrayList<Object>) wynik.get("text2");

                                    Object[] lista1 = list1.toArray();
                                    Object[] lista2 = list2.toArray();

                                    for(Object item: lista1)
                                    {
                                        ilosc++;
                                    }

                                    for(int i=0;i<ilosc;i++)
                                    {
                                        listMessages.add(new Message(lista1[i].toString(), lista2[i].toString(), 3));
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case "znajomi":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String all = "";
                                    int ilosc = 0;
                                    ArrayList<Object> list1 = (ArrayList<Object>) wynik.get("text1");
                                    ArrayList<Object> list2 = (ArrayList<Object>) wynik.get("text2");

                                    Object[] lista1 = list1.toArray();
                                    Object[] lista2 = list2.toArray();

                                    for(Object item: lista1)
                                    {
                                        ilosc++;
                                    }
                                    for(int i=0;i<ilosc;i++)
                                    {
                                        if(lista2[i].toString().equals("online"))
                                        {
                                            lista2[i] = "ONLINE";
                                        }
                                        all += lista1[i].toString() + ": " + lista2[i].toString() + "\n";
                                    }
                                    listMessages.add(new Message("Informacje o użytkownikach", all, 2));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case "online":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listMessages.add(new Message(wynik.get("text1").toString(), "jest teraz " + wynik.get("text2").toString(), 2));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case "wylacz":
                            finish();
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView lblLog = (TextView) findViewById(R.id.log);
                                    lblLog.setText("Nieznany pakiet: " + typ);
                                }
                            });
                            break;
                    }

                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uzupelnijDo(String str)
    {
        EditText txtDo = (EditText) findViewById(R.id.txtDo);
        if(txtDo.getText().toString().matches(""))
        {
            txtDo.setText(str);
        }
    }

    private void wczytajWiadomosci()
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("typ", "3");
            json.put("text1", "Client");
            json.put("text2", "Zadanie o wiadomosci");
            String jsonStr = json.toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(jsonStr);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void czyOnline()
    {
        try
        {
            JSONObject json = new JSONObject();
            json.put("typ", "5");
            json.put("text1", "Client");
            json.put("text2", "Zadanie o online");
            String jsonStr = json.toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(jsonStr);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}