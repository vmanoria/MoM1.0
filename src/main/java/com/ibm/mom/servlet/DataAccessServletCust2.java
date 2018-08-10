package com.ibm.mom.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import com.co3.dto.json.IncidentDTO;
import com.co3.dto.json.SessionOrgInfoDTO;
import com.co3.dto.json.UserSessionDTO;
import com.co3.simpleclient.SimpleClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ibm.db2.jcc.a.e;
import com.ibm.mom.data.model.Incident;
import com.ibm.simpleclient.SimpleClientEx;

/**
 * Servlet implementation class DataAccessServlet
 */
@WebServlet("/DataAccessServletCust")
public class DataAccessServletCust2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final TypeReference<List<IncidentDTO>> INC_TYPE = new TypeReference<List<IncidentDTO>>() {
	};
	private static final List<Object> networkZoneList = new ArrayList<Object>();
	private static final Map<Integer, String> networkznIdLableMap = new HashMap<Integer, String>();
	CompositeConfiguration config = new CompositeConfiguration();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataAccessServletCust2() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Determine what request
		String action = request.getParameter("whatdata");
		//loading the properties from the config.properties
		try {
			readProperties();
		} catch (Exception e1) {
			System.out.println("Not able to read the config properties"+e1.getMessage());
		}
		String restUrl=(String)config.getProperty("restUrl");
		String certPath= (String)config.getProperty("certpath");
		String certPass= (String)config.getProperty("certpass");
		String referehInterval= (String)config.getProperty("referehInterval");
		
		String loginId = "soc.manager@infosys.com";//(String) getServletContext().getAttribute("loginId");// "soc.manager@infosys.com";//UserLoginServlet.getLoginId(request);
		String pass_Word = "Pass4Admin";//(String) getServletContext().getAttribute("userpassword");
		
		SimpleClientEx simpleClient = null;
		URL url = new URL(restUrl);
		File certFilepath = new File(certPath);
		if (loginId != null && pass_Word != null) {
			simpleClient = new SimpleClientEx(url, null, loginId, pass_Word, certFilepath, certPass);
			simpleClient.connect(loginId, pass_Word);
		}
		if (action.equals("getChartData")) {

			List incidentTypeLst = new ArrayList();

			if (loginId != null && pass_Word != null && simpleClient != null) {

				try {
					UserSessionDTO getSessionInfo = simpleClient.getSessionInfo();
					List<String> orgNamelst = new ArrayList<String>();
					List<SessionOrgInfoDTO> orgs = getSessionInfo.getOrgs();

					for (SessionOrgInfoDTO org : orgs) {
						orgNamelst.add(org.getName());
					}

					List<Incident> incidentListForChart = new ArrayList<Incident>();
					Map<String, Map> chartDataIncidentMap = new HashMap<String, Map>();
					Map<String, Map> chartDataNetworkZoneMap = new HashMap<String, Map>();
					int openIncidents = 0, closedIncidents = 0, totalIncidents = 0, activeUsers = 0;
					for (String orgName : orgNamelst) {
						SimpleClient simpleClientForOrg = new SimpleClient(url, orgName, loginId, pass_Word,
								certFilepath, certPass);
						simpleClientForOrg.connect();
						// Getting the incidentData
						List<IncidentDTO> incidentsDataList = simpleClientForOrg.get("incidents?handle_format=names",
								INC_TYPE);

						for (IncidentDTO incidentch : incidentsDataList) {
							totalIncidents = totalIncidents + 1;
							Incident chartIncidentData = new Incident();

							if (incidentch.getPlanStatus() != null && incidentch.getPlanStatus().equals("A")) {
								openIncidents = openIncidents + 1;
								activeUsers = activeUsers + 1;

							} else {
								closedIncidents = closedIncidents + 1;

							}
							List<Object> lstAllIncType = incidentch.getIncidentTypeIds();
							for (Object inctype : lstAllIncType) {
								String IncidentTypeStr = (String) inctype;
								if (!incidentTypeLst.contains(IncidentTypeStr)) {
									incidentTypeLst.add(IncidentTypeStr);
								}
							}

							chartIncidentData.setDate_created(incidentch.getCreateDate());
							Calendar cal = Calendar.getInstance();
							cal.setTime(incidentch.getCreateDate());
							SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
							String datecreated = sdf.format(cal.getTime());
							Map prop = incidentch.getProperties();
							Map countNetworkZn = null;
							int countNz = 1;
							String networkString = "";
							List<Object> netinfo = null;
							try{
								netinfo = (ArrayList) prop.get("network_zone");
							}catch(Exception e){
								netinfo = null;
							}
							// Logic to create a networkZonechartData logic
							if (chartDataNetworkZoneMap.containsKey(datecreated)) {
								
								
								if (netinfo != null) {
									for (Object netzn : netinfo) {
										Map countexisting = chartDataNetworkZoneMap.get(datecreated);
										Integer networkZn = (Integer) netzn;
										if (networkznIdLableMap.get(networkZn) != null) {
											networkString = networkznIdLableMap.get(networkZn);
											if (countexisting.containsKey(networkString)) {

												int countval = (int) countexisting.get(networkString);
												int newcountval = countval + 1;

												countexisting.replace(networkString, countval, newcountval);
											} else {
												int count = 1;
												countexisting.put(networkString, count);
											}
										}
									}
								}

							} else {
								// count the network zone
								
								if (netinfo != null && netinfo.size() > 0) {
									countNetworkZn = new HashMap<>();
									for (Object netzn : netinfo) {
										Integer networkZn = (Integer) netzn;
										if (networkznIdLableMap.get(networkZn) != null) {
											networkString = networkznIdLableMap.get(networkZn);
											countNz = 1;
											countNetworkZn.put(networkString, countNz);
										}
									}
									chartDataNetworkZoneMap.put(datecreated, countNetworkZn);
								}
							}

							// End of Logic :- networkZonechartData logic
							// Logic to create a Incident type chart logic
							Map countIncidentType = null;
							int count = 1;
							if (chartDataIncidentMap.containsKey(datecreated)) {
								List<Object> lstIncType = incidentch.getIncidentTypeIds();

								if (lstIncType != null && !lstIncType.isEmpty()) {
									for (Object inctype : lstIncType) {
										Map countexisting = chartDataIncidentMap.get(datecreated);

										String IncidentType = (String) inctype;
										if (countexisting!=null && countexisting.containsKey(IncidentType)) {

											int countval = (int) countexisting.get(IncidentType);
											int newcountval = countval + 1;

											countexisting.replace(IncidentType, countval, newcountval);
										} else {
											count = 1;
											countexisting.put(IncidentType, count);
										}

									}
								}

							} else {
								// count the incident type
								List<Object> lstIncType = incidentch.getIncidentTypeIds();
								if(lstIncType!=null && !lstIncType.isEmpty()){
									for (Object inctype : lstIncType) {
										countIncidentType = new HashMap<>();
										String IncidentType = (String) inctype;
										count = 1;
										countIncidentType.put(IncidentType, count);
									}
									chartDataIncidentMap.put(datecreated, countIncidentType);
								}
							}
							// countIncidentType.put(incidentch.getIncidentTypeIds(),count);

							List<Object> lst = incidentch.getIncidentTypeIds();

							if (lst != null && !lst.isEmpty()) {
								chartIncidentData.setType((String) lst.get(0));
								incidentListForChart.add(chartIncidentData);
							}

						}

					}
					// End of Logic :-Incident type chartData logic
					JSONObject incidentDataObj = new JSONObject();
					incidentDataObj.put("openIncidents", openIncidents);
					incidentDataObj.put("closedIncidents", closedIncidents);
					incidentDataObj.put("totalIncidents", totalIncidents);
					incidentDataObj.put("activeUsers", activeUsers);
					JSONObject usersJson = new JSONObject();

					JSONArray chartArray = new JSONArray(incidentListForChart);
					usersJson.put("users",
							getChartJson(incidentTypeLst, chartDataIncidentMap, "Incident Over Time by Type"));
					usersJson.put("networkZnChartData",
							getChartJson(networkZoneList, chartDataNetworkZoneMap, "Network Zone"));
					usersJson.put("incidentData", incidentDataObj);

					usersJson.put("loginUser", loginId);
					response.setContentType("application/json");
					response.getWriter().write(usersJson.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (action.equals("getIncidentList")) {

			if (loginId != null && pass_Word != null && simpleClient != null) {
				try {

					UserSessionDTO getSessionInfo = simpleClient.getSessionInfo();
					Boolean isOnlyOneOrg = true;
					List<String> orgNamelst = new ArrayList<String>();
					List<SessionOrgInfoDTO> orgs = getSessionInfo.getOrgs();
					Map orgmapping = new HashMap();
					String headerincident = "Filter by Organisation";
					for (SessionOrgInfoDTO org : orgs) {
						orgNamelst.add(org.getName());
						orgmapping.put(org.getName(), org.getId());

					}
					if (orgNamelst != null && orgNamelst.size() == 1) {
						isOnlyOneOrg = false;
						headerincident = "Organisation : ";

					}

					List<Incident> incidentList = new ArrayList<Incident>();

					for (String orgName : orgNamelst) {
						SimpleClient simpleClientForOrg = new SimpleClient(url, orgName, loginId, pass_Word,
								certFilepath, certPass);
						simpleClientForOrg.connect();

						Map<Integer, String> attackIdLableMap = new HashMap<Integer, String>();
						Map<Integer, String> categoriesIdLableMap = new HashMap<Integer, String>();
						// Getting attack types information and creating a
						// Map(attacktypeid --> attacktypeName)
						try {

							Map<String, Object> attackTypeMap = simpleClientForOrg
									.get("types/incident/fields/attack_type?handle_format=names");

							if (attackTypeMap != null) {
								List<LinkedHashMap> attackTypeMapLst = (List<LinkedHashMap>) attackTypeMap
										.get("values");
								for (LinkedHashMap attackTypes : attackTypeMapLst) {
									Integer attackId = (Integer) attackTypes.get("value");
									String label = (String) attackTypes.get("label");
									attackIdLableMap.put(attackId, label);
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// End of getting attack type
						// Getting networkZone information
						try {
							Map<String, Object> networkzoneMap = simpleClientForOrg
									.get("types/incident/fields/network_zone?handle_format=names");

							if (networkzoneMap != null) {
								List<LinkedHashMap> networkzoneMapLst = (List<LinkedHashMap>) networkzoneMap
										.get("values");
								for (LinkedHashMap networkzn : networkzoneMapLst) {
									Integer valueId = (Integer) networkzn.get("value");
									String label = (String) networkzn.get("label");
									networkznIdLableMap.put(valueId, label);
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// Getting categories info
						try {
							Map<String, Object> categoriesMap = simpleClientForOrg
									.get("types/incident/fields/categories?handle_format=names");

							if (categoriesMap != null) {
								List<LinkedHashMap> categoriesMapLst = (List<LinkedHashMap>) categoriesMap
										.get("values");
								for (LinkedHashMap categoriesval : categoriesMapLst) {
									Integer valueId = (Integer) categoriesval.get("value");
									String label = (String) categoriesval.get("label");
									categoriesIdLableMap.put(valueId, label);
								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// Getting the incident data
						List<IncidentDTO> incidentsData = simpleClientForOrg.get("incidents?handle_format=names",
								INC_TYPE);

						for (IncidentDTO incident : incidentsData) {
							Incident inc = new Incident();
							Map prop = incident.getProperties();
							String attackString = "";
							String catString = "";
							String networkString = "";

							List<Object> lstattacktype = (ArrayList) prop.get("attack_type");

							if (lstattacktype != null) {
								for (Object attackid : lstattacktype) {
									Integer attack = (Integer) attackid;
									if (attackIdLableMap.get(attack) != null) {
										attackString = attackString + attackIdLableMap.get(attack);
									}
								}
							} else {
								attackString = "";
							}
							inc.setAttack_type(attackString);
							List<Object> categoriesInfoLst = (ArrayList) prop.get("categories");

							if (categoriesInfoLst != null) {
								for (Object categoriesInfo : categoriesInfoLst) {
									Integer catInfo = (Integer) categoriesInfo;
									if (categoriesIdLableMap.get(catInfo) != null) {
										catString = catString + categoriesIdLableMap.get(catInfo);
									}
								}
							} else {
								catString = "";
							}

							inc.setCategories(catString);
							List<Object> netinfo = (ArrayList) prop.get("network_zone");

							if (netinfo != null) {
								for (Object netzn : netinfo) {
									Integer networkZn = (Integer) netzn;
									if (networkznIdLableMap.get(networkZn) != null) {
										networkString = networkString + networkznIdLableMap.get(networkZn);
										if (networkZoneList != null && !networkZoneList.contains(networkString)) {
											networkZoneList.add((Object) networkString);
										}
									}
								}
							} else {
								networkString = "";
							}

							inc.setNetwork_zone(networkString);
							int id = incident.getId();
							inc.setId(id);
							int orgid = (int) orgmapping.get(orgName);
							String incUrl = restUrl + "#incidents/" + id;
							// "https://resilient/#incidents/"+id;
							String orgUrl = restUrl + "#org/switch/" + orgid;
							// "https://resilient/#org/switch/"+orgid;
							inc.setIncidentUrl(incUrl);
							inc.setName(incident.getName());
							inc.setOrgId((int) orgmapping.get(orgName));
							inc.setOrgUrl(orgUrl);
							inc.setDate_created(incident.getCreateDate());
							inc.setDate_discovered(incident.getDiscoveredDate());
							if (incident.getDescription() != null) {
								inc.setDescription(incident.getDescription().toString());
							}
							inc.setDue_date(incident.getDueDate());
							inc.setOraganisation(orgName);
							if (incident.getOwnerId() != null) {
								inc.setOwner(incident.getOwnerId().toString());
							}
							if (incident.getPhaseId() != null) {
								inc.setPhase(incident.getPhaseId().toString());
							}
							if (incident.getSeverityCode() != null) {
								inc.setSeverity(incident.getSeverityCode().toString());
							}

							String incidentStatus = incident.getPlanStatus();

							if (incidentStatus != null && incidentStatus.equals("A")) {
								inc.setStatus("Active");
							} else {
								inc.setStatus("Inactive");
							}

							incidentList.add(inc);
						}
					}
					int refreshTime = Integer.parseInt(referehInterval);
					JSONObject usersJson = new JSONObject();
					JSONArray usersArray = new JSONArray(incidentList);
					usersJson.put("users", usersArray);
					usersJson.put("isOnlyOneOrg", isOnlyOneOrg);
					usersJson.put("headerincident", headerincident);
					usersJson.put("orgname", orgNamelst);
					usersJson.put("loginUser", loginId);
					usersJson.put("referehInterval", refreshTime);
					response.setContentType("application/json");
					response.getWriter().write(usersJson.toString());
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		}

	}
	private void readProperties() throws Exception {
	        
	        // add config sources.
	        // add SystemConfiguration first below we need to override properties
	        // using java system properties
	        config.addConfiguration(new SystemConfiguration());
	        config.addConfiguration(new PropertiesConfiguration("/config.properties"));
	 
	        System.out.println("----------------------------");
	        System.out.println("Listing composite properties");
	        System.out.println("----------------------------");
	        Iterator<String> keys = config.getKeys();
	        while (keys.hasNext()) {
	            String key = keys.next();
	            System.out.println(key + " = " + config.getProperty(key));
	        }
	    }
	/**
	 * This method will create a JSON object require to generate the google
	 * chart
	 * 
	 * @param properties(eg:
	 *            incident type or network zone)
	 * @param DataMap
	 *            (Contains eg:DateCreated-->SubMap and SubMap will contain
	 *            incidentTypes --> counts)
	 * @param nameChart
	 *            (This is name of the chart)
	 * @return JSONObject
	 */
	private JSONObject getChartJson(List<Object> properties, Map<String, Map> chartDataMap, String namechart) {

		JSONObject dataObj = new JSONObject();
		JSONObject chartJson = new JSONObject();
		JSONObject optionJson = new JSONObject();
		JSONObject animationObj = new JSONObject();
		JSONObject colObj = new JSONObject();
		JSONArray colArray = new JSONArray();
		// Populating coloum information
		colObj.put("label", "Create_Date");
		colObj.put("type", "string");
		colArray.put(colObj);
		for (Object inct : properties) {
			String inctype = (String) inct;
			colObj = new JSONObject();
			colObj.put("label", inctype.toString());
			colObj.put("type", "number");
			colArray.put(colObj);
		}

		// populating rows information
		JSONArray rowArray = new JSONArray();
		List<String> sortedDataMapKeys = new ArrayList<String>(chartDataMap.keySet());
		Collections.sort(sortedDataMapKeys);
		for (String key : sortedDataMapKeys) {
			JSONArray cArray = new JSONArray();

			JSONObject valueObj = new JSONObject();
			JSONObject cObj = new JSONObject();

			valueObj.put("v", key.toString());
			cArray.put(valueObj);
			for (Object inct : properties) {
				valueObj = new JSONObject();
				String inctype = (String) inct;
				Map valuedata = chartDataMap.get(key);
				if (valuedata.containsKey(inctype)) {
					int countvalue = (int) valuedata.get(inctype);
					valueObj.put("v", countvalue);
				} else {
					valueObj.put("v", "null");
				}

				cArray.put(valueObj);
			}

			cObj.put("c", cArray);
			rowArray.put(cObj);
		}

		dataObj.put("cols", colArray);
		dataObj.put("rows", rowArray);

		// vertical axis
		JSONObject vaxis = new JSONObject();
		// horizontal axis
		JSONObject haxis = new JSONObject();

		// animation obj
		animationObj.put("startup", "true");
		animationObj.put("duration", 2000);
		animationObj.put("easing", "easing");
		vaxis.put("title", "Counts");
		haxis.put("title", "Date");

		// option Json
		optionJson.put("title", namechart);
		optionJson.put("isStacked", "true");
		optionJson.put("fill", 20);
		optionJson.put("interpolateNulls", true);
		optionJson.put("pointSize", 4);
		optionJson.put("is3D", "false");
		optionJson.put("displayExactValues", "false");
		optionJson.put("vAxis", vaxis);
		optionJson.put("hAxis", haxis);
		chartJson.put("type", "ColumnChart");
		chartJson.put("displayed", false);
		chartJson.put("cssStyle", "height:600px;width: 100%");
		chartJson.put("data", dataObj);
		chartJson.put("options", optionJson);
		return chartJson;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
