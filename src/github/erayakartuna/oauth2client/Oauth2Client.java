package github.erayakartuna.oauth2client;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import github.erayakartuna.oauth2client.Grant.AbstractGrant;
import github.erayakartuna.oauth2client.Grant.Password;
import github.erayakartuna.oauth2client.Grant.RefreshToken;
import github.erayakartuna.oauth2client.Token.Token;
import github.erayakartuna.oauth2client.Tools.HttpRequest;
import github.erayakartuna.oauth2client.Tools.RequestFinishListener;

/**
 * Created by Eray Akartuna on 12/04/17.
 */

public class Oauth2Client {

    protected Consts consts;
    protected Context context;
    protected TokenListener finishListener;
    protected Token token;


    public Oauth2Client(Context context)
    {
        this.context = context;
        this.consts = new Consts();
        this.finishListener = null;
        this.token = new Token(context);
    }

    public Oauth2Client(Context context, TokenListener finishListener)
    {
        this.context = context;
        this.consts = new Consts();
        this.finishListener = finishListener;
        this.token = new Token(context);
    }

    /**
     * Check Acces Token
     * @param finishListener
     */
    public void checkAccessToken(TokenListener finishListener)
    {
        this.finishListener = finishListener;

        Token token = new Token(this.context);
        if(token.isExpired())
        {
            if(token.getRefreshToken() != null)
            {
                refreshToken(token.getRefreshToken());
            }
            else{
                setFinishListener(false);
            }
        }
        else{
            setFinishListener(true);
        }
    }

    /**
     * Get Access Token
     *
     * @param email
     * @param password
     * @param finishListener
     */
    public void login(String email,String password,TokenListener finishListener)
    {
        this.finishListener = finishListener;

        HashMap<String,String> urlParameters = new HashMap<>();
        urlParameters.put("username",email);
        urlParameters.put("password",password);
        urlParameters.put("client_id",this.consts.client_id);
        urlParameters.put("client_secret",this.consts.client_secret);
        AbstractGrant grant = new Password();

        try{
            urlParameters = grant.prepareRequestParameters(urlParameters);
            String targetURL = this.consts.site_url+"/"+this.consts.login_uri;
            new HttpRequest(targetURL, "POST", urlParameters, new RequestFinishListener() {
                @Override
                public void onFinishRequest(String Response) {
                    try {
                        JSONObject obj = new JSONObject(Response);
                        if(obj.has("error"))
                        {
                            setFinishListener(false);
                        }
                        else{
                            setToken(Response);
                            setFinishListener(true);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        setFinishListener(false);
                    }
                }
            }).executeRequest();
        }
        catch (Exception e){
            e.printStackTrace();
            setFinishListener(false);
            e.getMessage();
        }

    }

    /**
     * Refresh Token
     */
    public void refreshToken()
    {
        Token token = new Token(this.context);
        refreshToken(token.getRefreshToken());
    }

    /**
     * Get Access Token By Refresh Token
     * @param refresh_token
     */
    public void refreshToken(String refresh_token)
    {
        HashMap<String,String> urlParameters = new HashMap<>();
        urlParameters.put("refresh_token",refresh_token);
        urlParameters.put("client_id",this.consts.client_id);
        urlParameters.put("client_secret",this.consts.client_secret);
        AbstractGrant grant = new RefreshToken();
        try{
            urlParameters = grant.prepareRequestParameters(urlParameters);
            String targetURL = this.consts.site_url+"/"+this.consts.refresh_uri;
            new HttpRequest(targetURL, "POST", urlParameters, new RequestFinishListener() {
                @Override
                public void onFinishRequest(String Response) {
                    try {
                        JSONObject obj = new JSONObject(Response);
                        if(obj.has("error"))
                        {
                            setFinishListener(false);
                        }
                        else{
                            setToken(Response);
                            setFinishListener(true);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        setFinishListener(false);
                    }
                }
            }).executeRequest();
        }
        catch (Exception e){
           e.printStackTrace();
            setFinishListener(false);
        }
    }

    /**
     * Set Access Token from json
     * @param Response
     */
    protected void setToken(String Response)
    {
        try {
            JSONObject obj = new JSONObject(Response);
            new Token(obj.getString("access_token"),obj.getString("refresh_token"),obj.getDouble("expires_in"),this.context);
        }
        catch (JSONException e){
            e.printStackTrace();
            setFinishListener(false);
        }
    }

    public void logout()
    {
        this.token.setAccessToken(null);
        this.token.setRefreshToken(null);
        this.token.setExpiresAt(0);
        this.token.setExpiresIn(0);
    }

    /**
     * Set Observer Finish
     * @param status
     */
    public void setFinishListener(boolean status)
    {
        this.finishListener.setOnFinishListener(status);
    }

    public void getResource(String target_uri,String method,HashMap<String,String> parameters,RequestFinishListener finishListener)
    {
        parameters.put("access_token",this.token.getAccessToken());
        new HttpRequest(this.consts.site_url+"/"+target_uri, method, parameters,finishListener).executeRequest();
    }

    public void getResource(String target_uri,String method,RequestFinishListener finishListener)
    {
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("access_token",this.token.getAccessToken());
        new HttpRequest(this.consts.site_url+"/"+target_uri, method, parameters,finishListener).executeRequest();
    }

    public void getResource(String target_uri,String method)
    {
        HashMap<String,String> parameters = new HashMap<>();
        parameters.put("access_token",this.token.getAccessToken());
        new HttpRequest(this.consts.site_url+"/"+target_uri, method, parameters).executeRequest();
    }
}
