package github.erayakartuna.oauth2client.Grant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eray on 12/04/17.
 */

public abstract class AbstractGrant {

    public abstract String getName();

    public abstract ArrayList<String> getRequiredRequestParameters();

    public String toString()
    {
        return this.getName();
    }

    protected boolean checkRequiredParameters(HashMap<String,String> parameters)
    {
        ArrayList<String> requireds = getRequiredRequestParameters();
        for(int i=0;i<requireds.size();i++)
        {
            if(parameters.get(requireds.get(i)) == null)
            {
                return false;
            }
        }

        return true;
    }

    public HashMap<String,String> prepareRequestParameters(HashMap<String,String> parameters)
    {
        if(!checkRequiredParameters(parameters))
        {
            throw new IllegalArgumentException("Required parameters not passed");
        }

        parameters.put("grant_type",this.getName());
        return parameters;
    }
}
