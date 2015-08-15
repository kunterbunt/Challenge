package sebastians.challenge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import sebastians.challenge.data.UserCredentials;
import sebastians.challenge.networking.UserSvc;
import sebastians.challenge.tools.MyCredentials;


/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileActivityFragment extends Fragment {

    Button save;
    TextView username;
    private SharedPreferences sharedPref;
    UserCredentials userCreds;
    public UserProfileActivityFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = this.getActivity().getSharedPreferences(MyCredentials.SHRDPREFID, Context.MODE_PRIVATE);
        userCreds = new UserCredentials(sharedPref);
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        // Set list view adapter.
        username = (TextView) view.findViewById(R.id.username_txt);
        save = (Button) view.findViewById(R.id.save_btn);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String usernameStr = username.getText().toString();

                        if (userCreds.isTokenExpired())
                            userCreds.renewToken();

                        //update username
                        TTransport transport = new TFramedTransport(new TSocket("192.168.178.91", 9090));
                        try {

                            transport.open();
                            TProtocol protocol = new TBinaryProtocol(transport);
                            TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "User");
                            UserSvc.Client client = new UserSvc.Client(mp);
                            client.updateUser(userCreds.getToken(), usernameStr);

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
