package com.cs.layer4.persistence.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableJoiner {

	Map<String, Map<String, String>> primaryTableConditionInfo;
	List<String> returnColumns;
	Map<String, Map<String, String>> secondaryTableConditionInfo;
	Map<String, Map<String, String>> primarySecondaryMappingInfo;

	public Map<String, Map<String, String>> getPrimaryTableConditionInfo() {
		return primaryTableConditionInfo;
	}

	public void setPrimaryTableConditionInfo(
			Map<String, Map<String, String>> primaryTableConditionInfo) {
		this.primaryTableConditionInfo = primaryTableConditionInfo;
	}

	public List<String> getReturnColumns() {
		return returnColumns;
	}

	public void setReturnColumns(List<String> returnColumns) {
		this.returnColumns = returnColumns;
	}

	public Map<String, Map<String, String>> getSecondaryTableConditionInfo() {
		return secondaryTableConditionInfo;
	}

	public void setSecondaryTableConditionInfo(
			Map<String, Map<String, String>> secondaryTableConditionInfo) {
		this.secondaryTableConditionInfo = secondaryTableConditionInfo;
	}

	public Map<String, Map<String, String>> getPrimarySecondaryMappingInfo() {
		return primarySecondaryMappingInfo;
	}

	public void setPrimarySecondaryMappingInfo(
			Map<String, Map<String, String>> primarySecondaryMappingInfo) {
		this.primarySecondaryMappingInfo = primarySecondaryMappingInfo;
	}

	@Override
	public String toString() {
		String query = "SELECT primary.";
		Set<String> primaryTableKeySet = primaryTableConditionInfo.keySet();
		String primaryTableName = "";
		if(primaryTableKeySet != null && primaryTableKeySet.iterator().hasNext()){
			primaryTableName = primaryTableKeySet.iterator().next();
		}
		if(primaryTableName != null && !primaryTableName.isEmpty()){
			query += primaryTableName + " from";
		}
		else{
			query += "* from ";
		}
		Set<String> secondaryTablesSet = secondaryTableConditionInfo.keySet();
		for (String secondaryTableName : secondaryTablesSet) {
			
		}
		return query;
	}

	public String query() {
		return this.toString();
	}

}
