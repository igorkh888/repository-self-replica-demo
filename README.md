#Installation

### 1. Prepare server
To run this app you should have server with publicly available domain or IP 
address, this is needed for Github OAuth 
[Web application flow](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/).

### 2. Install Git
If you don't have Git installed, please install it first.
https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

### 3. Install Maven
If you don't have Maven installed, please install it first.
https://maven.apache.org/install.html

### 4. Clone application source code
```
git clone git@github.com:xrom888/repository-self-replica-demo.git
```

### 5. Create Github app
To access to Github api, we need register application here: 
https://github.com/settings/applications/new.

After you done, you will get `Client ID` and `Client Secter`.

Set this values to `application.properties` file which you can find in path: 
`src/main/resources`.

Or you can set this properties as environment variables when you run the app.

In redirect URL section you need to set you public IP address or domain name 
with redirect URI value, like:
```
    8.8.8.8:8888/user/github/oauth_code
```

!!! If you will change in application redirect URI value 
(default one `/user/github/oauth_code`), you need also to change it in 
registered app properties.

### 6.Set server host
By default server host is not set.
You need to set IP address or domain name in `application.properties` same as 
you set when registered app on Github.

So for example it can be:
```
    server.host=8.8.8.8
```

### 7. Set application port
By default the application (Tomcat) port port set to `8888`.

Before start the application, check if this port not used by any other app.

On Mac you can run: 
```
lsof -nP -i4TCP:8888
```

If port is already used by some other app, you can change port number in 
`application.properties`.

### 8. Set SSL option
If your server uses SSL certificate, please set property:
 ```
 server.use.ssl=true
 ```
 
if not:
 ```
 server.use.ssl=false
 ```

Default value: `false`

### 9. Set author name and email (optional)
If you want, you can change application author name and email in 
`application.properties`.



### 10. Run application

To run the app you need to go to app dir and execute command:
```
mvn spring-boot:run
```

#Use app

### 1. Sign in with Github
To create this app replica, you need to give access to the app to your own 
github account with permissions (:read and :write for public repos).

1. To do that, just open app url and click `Sign in with Github` button.
2. You will be redirected to Github with question to give access for registered app 
with list of permissions.
3. Click `Authorize [Github user]`.

### 2. Create app replica
After that you will be redirected to the app and you will see green button `Create replica`.

Press it to create replica of the app.

Open your github account and check if repo fork exists.

### 3. Logout
If you want to logout, just click logout button at the top right corner 
of the website.

Enjoy!