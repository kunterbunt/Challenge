package sebastians.challenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.security.Security;

import sebastians.challenge.data.UserCredentials;
import sebastians.challenge.networking.ThriftToken;
import sebastians.challenge.networking.UserSvc;
import sebastians.challenge.tools.MyCredentials;
import sebastians.challenge.tools.Prefs;
import sebastians.challenge.tools.Utis;


/**
 * A placeholder fragment containing a simple view.
 */
public class IdentitiyVerificationActivityFragment extends Fragment {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public final String TAG = "Verification";
    private Button newUser;
    private Button reqToken;
    private SharedPreferences sharedPref;
    public IdentitiyVerificationActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = this.getActivity().getSharedPreferences(MyCredentials.SHRDPREFID, Context.MODE_PRIVATE);



        final View view = inflater.inflate(R.layout.fragment_identitiy_verification, container, false);
        newUser = (Button) view.findViewById(R.id.create_user);
        reqToken = (Button) view.findViewById(R.id.reqtoken);


        if(!sharedPref.getString(Prefs.USER,"").equals(""))
           ;// newUser.setEnabled(false);

        newUser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                //newUser.setEnabled(false);
                //request friends from server
                AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        TTransport transport;
                        try {
                            transport = new TFramedTransport(new TSocket("192.168.178.91", 9090));
                            transport.open();
                            TProtocol protocol = new TBinaryProtocol(transport);
                            TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "User");
                            UserSvc.Client client = new UserSvc.Client(mp);

                            //save this stuff in shared preferences and we are done!
                            String password = Utis.getRandomString();
                            String userId = client.createUser(password);

                            //
                            SharedPreferences.Editor edit = sharedPref.edit();
                            edit.putString(Prefs.USER,userId);
                            edit.putString(Prefs.PW,password);
                            edit.apply();


                            Log.i(TAG, userId + " " + password);

                            transport.close();

                        } catch (Exception x) {
                            x.printStackTrace();
                        }

                        return null;
                    }
                };
                asyncTask.execute("");
            }
        });


        reqToken.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                //newUser.setEnabled(false);
                //request friends from server
                AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        TTransport transport= new TFramedTransport(new TSocket("192.168.178.91", 9090));
                        try {

                            transport.open();
                            TProtocol protocol = new TBinaryProtocol(transport);
                            TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "User");
                            UserSvc.Client client = new UserSvc.Client(mp);

                            UserCredentials userCredentials = new UserCredentials(sharedPref);
                            ThriftToken thriftToken = client.requestToken(userCredentials.getUsername(),userCredentials.getPassword());
                            //read and transform token information
                            String token = thriftToken.getToken();
                            long validity =System.currentTimeMillis() + thriftToken.getValidityDuration();
                            SharedPreferences.Editor edit = sharedPref.edit();
                            edit.putString(Prefs.TOKEN, token);
                            edit.putLong(Prefs.TOKENVALIDITY, validity);
                            edit.apply();

                            Log.i(TAG,"token; " + token);
                            Log.i(TAG, "validity; " + validity);




                        } catch (Exception x) {
                            x.printStackTrace();
                        } finally {
                            transport.close();
                        }

                        return null;
                    }
                };
                asyncTask.execute("");
            }
        });




        return view;
    }
}
