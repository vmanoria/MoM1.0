package com.ibm.simpleclient;

import java.io.File;
import java.net.URL;

import com.co3.dto.json.UserSessionDTO;
import com.co3.simpleclient.ServerConfig;
import com.co3.simpleclient.SimpleClient;
import com.co3.web.rest.json.AuthenticationDTO;
import com.fasterxml.jackson.core.type.TypeReference;

public class SimpleClientEx extends SimpleClient{

	public SimpleClientEx(ServerConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	public SimpleClientEx(URL baseURL, String orgName, String email, String password, File trustedStoreFile,
			String trustedStorePassword) {
		super(baseURL, orgName, email, password, trustedStoreFile, trustedStorePassword);
		// TODO Auto-generated constructor stub
	}

	public SimpleClientEx(URL baseURL, String orgName, String email, String password) {
		super(baseURL, orgName, email, password);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * The user session information.  This is returned by the Co3 server after successful
     * authentication.
     */
    private UserSessionDTO sessionInfo;

    /**
	 * @return the sessionInfo
	 */
	public UserSessionDTO getSessionInfo() {
		return sessionInfo;
	}
	
	/**
     * Internal method to authenticate to the server and load it's initial const data.
     */
    public void connect(String loginId ,String password) {
        AuthenticationDTO auth = new AuthenticationDTO();

        auth.setEmail(loginId);
        auth.setPassword(password);
        sessionInfo = post("/rest/session", auth, new TypeReference<UserSessionDTO>() {
        });       
    }
}
