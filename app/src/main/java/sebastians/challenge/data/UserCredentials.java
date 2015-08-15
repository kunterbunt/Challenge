package sebastians.challenge.data;

import android.content.SharedPreferences;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import sebastians.challenge.networking.ThriftToken;
import sebastians.challenge.networking.UserSvc;
import sebastians.challenge.tools.Prefs;

/**
 * little wrapper for userinformation!
 */
public class UserCredentials {
    private String username = "";
    private String password = "";
    private long tokenValidity;
    private SharedPreferences mPrefs;
    private String token;
    /**
     * read userinformation from preferences!
     * @param frompref
     */
    public UserCredentials(SharedPreferences frompref){
        username = frompref.getString(Prefs.USER,"");
        password = frompref.getString(Prefs.PW,"");
        tokenValidity = frompref.getLong(Prefs.TOKENVALIDITY, 0);
        token = frompref.getString(Prefs.TOKEN, "");
        mPrefs = frompref;

    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean isTokenExpired(){
        return tokenValidity < System.currentTimeMillis();
    }

    public String getToken(){
        return this.token;
    }

    public void renewToken(){

            TTransport transport= new TFramedTransport(new TSocket("192.168.178.91", 9090));
            try {

                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "User");
                UserSvc.Client client = new UserSvc.Client(mp);

                ThriftToken thriftToken = client.requestToken(this.getUsername(),this.getPassword());
                //read and transform token information
                String token = thriftToken.getToken();
                long validity =System.currentTimeMillis() + thriftToken.getValidityDuration();
                SharedPreferences.Editor edit = mPrefs.edit();
                edit.putString(Prefs.TOKEN, token);
                edit.putLong(Prefs.TOKENVALIDITY, validity);
                edit.apply();

                this.tokenValidity = validity;
                this.token = token;

            } catch (Exception x) {
                x.printStackTrace();
            } finally {
                transport.close();
            }


    }
}
