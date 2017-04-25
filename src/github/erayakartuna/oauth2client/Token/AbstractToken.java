package github.erayakartuna.oauth2client.Token;

/**
 * Created by eray on 14/04/17.
 */

public abstract class AbstractToken{

    public abstract void setRefreshToken(String refreshToken);

    public abstract String getRefreshToken();

    public abstract void setAccessToken(String accessToken);

    public abstract String getAccessToken();

    public abstract void setExpiresIn(double expiresIn);

    public abstract double getExpiresIn();

    public abstract double getExpiresAt();

    public abstract void setExpiresAt(double expiresAt);

    public double calculateExpiresAt(double expiresIn) {
        return (expiresIn * 1000) + System.currentTimeMillis();
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() >= this.getExpiresAt()) ? true : false;
    }
}
