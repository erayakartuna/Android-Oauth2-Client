# Android-Oauth2-Client
This package provides simple oauth2 management.

## Usage

#### Set To Consts.class 

```java
 public static String site_url = "http://akdemistanbul.com.tr";
    public static String refresh_uri = "oauth2/refresh_token";
    public static String login_uri = "oauth2/login";
    public static String client_id = "client2";
    public static String client_secret = "pass2";
```

### Import two classes
```java
 import github.erayakartuna.oauth2client.Oauth2Client;
 import github.erayakartuna.oauth2client.TokenListener;
```

#### Login User
```java

AccessToken accessToken = new AccessToken(this);
accessToken.login(email, password,new TokenListener() {
                @Override
                public void setOnFinishListener(boolean status) {
                    if(status == true)
                    {
                      System.out.println("login is successful");
                    }
                    else{
                      System.out.println("Error");
                    }
                }
            });

```

#### Check Access Token

You don't need to think about expire times.If access token has expired it will automatically refresh with refresh token.Just use checkAccessToken method.

```java
 AccessToken accessToken = new AccessToken(this);

        accessToken.checkAccessToken(new TokenListener() {
            @Override
            public void setOnFinishListener(boolean status) {
                if(status == true)
                {
                   System.out.println("You have an Access Token");
                }
                else{
                  System.out.println("You don't have an Access Token");
                }
            }
        });
```

#### Get Resource

You can use getResource method to reach resources.You don't need to know access token.The method will attach access token in your query. 


```java
  
   AccessToken accessToken = new AccessToken(this);
   
    //example 1
      accessToken.getResource("uri/test", "GET", new RequestFinishListener() {
            @Override
            public void onFinishRequest(String Response) {
                System.out.println(Response);
            }
        });
        
   //example 2
   HashMap<String,String> parameters = new HashMap<>();
   parameters.put("example_params","312312");
   accessToken.getResource("uri/test2", "POST", parameters, new RequestFinishListener() {
            @Override
            public void onFinishRequest(String Response) {
                System.out.println(Response);
            }
        });
        
   //example 3
   accessToken.getResource("uri/test3", "GET");
        
```


