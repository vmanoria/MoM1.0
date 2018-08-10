package com.ibm.mom.servlet;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.co3.dto.json.FullIncidentDataDTO;
import com.co3.dto.json.IncidentPIIDTO;
import com.co3.dto.json.IncidentPermsDTO;
import com.co3.dto.json.JustUserDTO;
import com.co3.simpleclient.SimpleClient;
import com.fasterxml.jackson.core.type.TypeReference;



public class QradarIntegeration {
	private static final TypeReference<FullIncidentDataDTO> FULL_INC_DATA = new TypeReference<FullIncidentDataDTO>() {
	};
	 
	
	public static void main(String[] args) {
		
		
		try {
			URL url = new URL("https://resilient/");
			File fs = new File("C:/infosysGarage/res.jks") ;
			//Get the organization name <<orgName>>from json and pass it to SimpleClient to create an instance
			String orgName ="Senate Rebel, Inc.";
			orgName ="ABC Corp";
			orgName ="ACME Corp";
			SimpleClient simpleClient= new SimpleClient(url, orgName, "soc.manager@infosys.com", "Pass4Admin" ,fs,"123456");
			simpleClient.connect();
				
			//setting up the basic data to create an incident in Resilient
			FullIncidentDataDTO fullIncidentData= setFullIncidentData();
			
			//Creating an incident using incident post call
			FullIncidentDataDTO newIncident = simpleClient.post("incidents",fullIncidentData,FULL_INC_DATA);
			
			//Printing the new incident id 
			System.out.println("done"+newIncident.getId());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//setting up the basic data require to create an incident in Resilient
	private static FullIncidentDataDTO setFullIncidentData(){
		FullIncidentDataDTO fullIncidentData = new FullIncidentDataDTO();
		
		//TODO : Set the Name to QRadar offense id, offense description, offense source
		fullIncidentData.setName("Creating through sample App for testing acmecorp with phase id");
		
		//TODO :Set the description to offense event count, offense description
		fullIncidentData.setDescription("Event: Ransomware Behavior Creating through sample App");
		
		//TODO : Verify phase id in all org
		fullIncidentData.setPhaseId(1038);
		
		fullIncidentData.setCreator(null);
		
		//Setting the incident type to System Intrusion
		List _incidentTypeIds = new ArrayList<>();
		_incidentTypeIds.add(20);
		
		fullIncidentData.setIncidentTypeIds(_incidentTypeIds);
		fullIncidentData.setReporter(null);
		
		fullIncidentData.setPii(null);
		fullIncidentData.setPlanStatus("A");
		fullIncidentData.setDueDate(null);
		Date _discoveredDate = new Date();
		_discoveredDate.setTime(1476361680);
		Date _createDate = new Date();
		_createDate.setTime(1494243331);
		fullIncidentData.setCreateDate(_createDate);
		fullIncidentData.setDiscoveredDate(_discoveredDate);
		Date _startDate = new Date();
		_startDate.setTime(1476361680);
		fullIncidentData.setStartDate(_startDate);		
		fullIncidentData.setPerms(null);
		return fullIncidentData;
		
	}

}
