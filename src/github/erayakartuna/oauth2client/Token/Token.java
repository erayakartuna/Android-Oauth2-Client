package github.erayakartuna.oauth2client.Token;

import android.content.Context;
import android.content.SharedPreferences;

import github.erayakartuna.oauth2client.R;

/**
 * Created by eray on 14/04/17.
 */

public class Token extends AbstractToken{
    protected String refreshToken;
    protected String accessToken;
    protected double expiresIn;
    protected double expiresAt;
    protected Context context;
    protected SharedPreferences shp;
    protected SharedPreferences.Editor ed;

    public Token(Context context)
    {
        shp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        ed  = shp.edit();
        setContext(context);
    }

    public Token(String accessToken,String refreshToken,Double expiresIn,Context context)
    {
        shp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        ed  = shp.edit();

        setContext(context);
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setExpiresIn(expiresIn);
        setExpiresAt(calculateExpiresAt(expiresIn));
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        ed.putString("refreshToken",refreshToken);
        ed.commit();
    }

    @Override
    public String getRefreshToken() {
        return shp.getString("refreshToken",null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        ed.putString("github/erayakartuna/accessToken",accessToken);
        ed.commit();
    }

    @Override
    public String getAccessToken() {
        return shp.getString("github/erayakartuna/accessToken",null);
    }

    @Override
    public void setExpiresAt(double expiresAt) {
        ed.putString("expiresAt",Double.toString(expiresAt));
        ed.commit();
    }

    @Override
    public double getExpiresAt() {
        return Double.parseDouble(shp.getString("expiresAt","0"));
    }

    @Override
    public void setExpiresIn(double expiresIn) {
        ed.putString("expiresIn",Double.toString(expiresIn));
        ed.commit();
    }

    @Override
    public double getExpiresIn() {
        return Double.parseDouble(shp.getString("expiresIn","0"));
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

}
