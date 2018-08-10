package com.ibm.mom.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import com.ibm.simpleclient.SimpleClientEx;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * Servlet implementation class UserLoginServlet
 */
@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public static String LOGIN_ID = "LoginID";
    public static String PASSWORD = "password";
    public static String USER_NAME = "UserName";
    public static String USER_ID = "UserID";
   
    private static final String CONFIG_FILE_NAME = "config.properties";
    CompositeConfiguration config = new CompositeConfiguration();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserLoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginId = request.getParameter("uname");
		String password = request.getParameter("upwd");
		
		
		String pageURL = "/index.html";
		/*
		  * First, create (or ask some other component for) the adequate encryptor for
		  * decrypting the values in our .properties file.
		  */
		 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		 encryptor.setPassword("mom"); // could be got from web, env variable...
		 
		 /*
		  * Create our EncryptableProperties object and load it the usual way.
		  */
		
		SimpleClientEx simpleClient= null;
		String restUrl= null;
		String certPath=null;
		String certPass=null;
		String referehInterval=null;	
		
		try{
			readProperties();
			restUrl=(String)config.getProperty("restUrl");
			certPath= (String)config.getProperty("certpath");
			certPass= (String)config.getProperty("certpass");
			certPass = encryptor.decrypt(certPass); 
			referehInterval= (String)config.getProperty("referehInterval");
			URL url = new URL(restUrl);
			File fs = new File(certPath) ;

			//Creating a instance of SimpleClient with organization null.
			simpleClient= new SimpleClientEx(url, null, loginId, password ,fs,certPass);	
	        simpleClient.connect(loginId, password); 
			
	        pageURL = "/incidentListView.html";
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("exception occur "+e);
			pageURL = "/index.html";
		}
        
		HttpSession session = request.getSession();		
        session.setAttribute(LOGIN_ID, loginId);
        session.setAttribute(PASSWORD, password);         
        getServletContext().setAttribute("loginId", loginId);
        getServletContext().setAttribute("userpassword", password);
        getServletContext().setAttribute("restUrl", restUrl);
        getServletContext().setAttribute("certPath", certPath);
        getServletContext().setAttribute("certPass", certPass);
        getServletContext().setAttribute("referehInterval", referehInterval);

        RequestDispatcher dispatcher = request.getRequestDispatcher(pageURL);
		dispatcher.forward(request,response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * Use this method to add login ID and logged in user name to the data
	 */
	public static void addSession(HttpServletRequest request, JSONObject jsonObj){
		//Add session content to it
		HttpSession session = request.getSession(false);
		if (session == null) {
		    // Not created yet. Now do so yourself.
		    session = request.getSession();
		}
		String sessionJson = "{loginId: \"" + session.getAttribute(LOGIN_ID) +
		"\", userName: \"" + session.getAttribute(USER_NAME) + "\"}";
		
		jsonObj.put("session", sessionJson);	
	}
	
	/**
	 * Get the logged in user's login ID
	 */
	public static String getLoginId(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
		    // Not created yet. Now do so yourself.
		    session = request.getSession();
		}
		return (String) session.getAttribute(LOGIN_ID);
	}
	
	public static HttpSession getSessionInfo(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		
		return  session;
	}
	
	/**
	 * Get the logged in user's Password
	 */
	public static String getPassword(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
		    // Not created yet. Now do so yourself.
		    session = request.getSession();
		}
		return (String) session.getAttribute(PASSWORD);
	}
	/**
	 * Get the logged in user's login ID
	 */
	public static Integer getLoggedInUserId(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session == null) {
		    // Not created yet. Now do so yourself.
		    session = request.getSession();
		}
		return (Integer) session.getAttribute(USER_ID);
	}
	
	
	private void readProperties() throws Exception {
        
        // add config sources.
        // add SystemConfiguration first below we need to override properties
        // using java system properties
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration("/config.properties"));

        Iterator<String> keys = config.getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println(key + " = " + config.getProperty(key));
        }
    }

	
	}
