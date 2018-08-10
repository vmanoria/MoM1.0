package com.ibm.mom.data.model;

import java.util.Date;

public class Incident {

	private int id;
	private String name;
	private String description;
	private Date date_discovered;
	private Date due_date;
	private Date date_created;
	private String oraganisation;
	private String owner;
	private String phase;
	private String severity;
	private String status;
	private String attack_type;
	private String categories;
	private String network_zone;
	private String incidentUrl;
	private String type;
	private int orgId;
	private String orgUrl;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attack_type == null) ? 0 : attack_type.hashCode());
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((date_created == null) ? 0 : date_created.hashCode());
		result = prime * result + ((date_discovered == null) ? 0 : date_discovered.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((due_date == null) ? 0 : due_date.hashCode());
		result = prime * result + id;
		result = prime * result + ((incidentUrl == null) ? 0 : incidentUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((network_zone == null) ? 0 : network_zone.hashCode());
		result = prime * result + ((oraganisation == null) ? 0 : oraganisation.hashCode());
		result = prime * result + orgId;
		result = prime * result + ((orgUrl == null) ? 0 : orgUrl.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		result = prime * result + ((severity == null) ? 0 : severity.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Incident other = (Incident) obj;
		if (attack_type == null) {
			if (other.attack_type != null)
				return false;
		} else if (!attack_type.equals(other.attack_type))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (date_created == null) {
			if (other.date_created != null)
				return false;
		} else if (!date_created.equals(other.date_created))
			return false;
		if (date_discovered == null) {
			if (other.date_discovered != null)
				return false;
		} else if (!date_discovered.equals(other.date_discovered))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (due_date == null) {
			if (other.due_date != null)
				return false;
		} else if (!due_date.equals(other.due_date))
			return false;
		if (id != other.id)
			return false;
		if (incidentUrl == null) {
			if (other.incidentUrl != null)
				return false;
		} else if (!incidentUrl.equals(other.incidentUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (network_zone == null) {
			if (other.network_zone != null)
				return false;
		} else if (!network_zone.equals(other.network_zone))
			return false;
		if (oraganisation == null) {
			if (other.oraganisation != null)
				return false;
		} else if (!oraganisation.equals(other.oraganisation))
			return false;
		if (orgId != other.orgId)
			return false;
		if (orgUrl == null) {
			if (other.orgUrl != null)
				return false;
		} else if (!orgUrl.equals(other.orgUrl))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		if (severity == null) {
			if (other.severity != null)
				return false;
		} else if (!severity.equals(other.severity))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	public String getOrgUrl() {
		return orgUrl;
	}
	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIncidentUrl() {
		return incidentUrl;
	}
	public void setIncidentUrl(String incidentUrl) {
		this.incidentUrl = incidentUrl;
	}
	public String getAttack_type() {
		return attack_type;
	}
	public void setAttack_type(String attack_type) {
		this.attack_type = attack_type;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getNetwork_zone() {
		return network_zone;
	}
	public void setNetwork_zone(String network_zone) {
		this.network_zone = network_zone;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the date_discovered
	 */
	public Date getDate_discovered() {
		return date_discovered;
	}
	/**
	 * @param date_discovered the date_discovered to set
	 */
	public void setDate_discovered(Date date_discovered) {
		this.date_discovered = date_discovered;
	}
	/**
	 * @return the due_date
	 */
	public Date getDue_date() {
		return due_date;
	}
	/**
	 * @param due_date the due_date to set
	 */
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	/**
	 * @return the date_created
	 */
	public Date getDate_created() {
		return date_created;
	}
	/**
	 * @param date_created the date_created to set
	 */
	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
	/**
	 * @return the oraganisation
	 */
	public String getOraganisation() {
		return oraganisation;
	}
	/**
	 * @param oraganisation the oraganisation to set
	 */
	public void setOraganisation(String oraganisation) {
		this.oraganisation = oraganisation;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}
	/**
	 * @param phase the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}
	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}
