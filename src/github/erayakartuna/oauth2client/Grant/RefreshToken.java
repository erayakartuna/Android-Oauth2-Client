package github.erayakartuna.oauth2client.Grant;

import java.util.ArrayList;

/**
 * Created by eray on 13/04/17.
 */

public class RefreshToken extends AbstractGrant {

    @Override
    public String getName()
    {
        return "refresh_token";
    }

    public ArrayList<String> getRequiredRequestParameters() {
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("refresh_token");
        parameters.add("client_id");
        parameters.add("client_secret");

        return parameters;
    }
}
