package com.cs.layer4.persistence.query;

import java.util.HashMap;
import java.util.Map;

public class NestedQuery {

	Map<String, Map<String, String>> subTableNames;

	String targetTableName;

	String db;

	/*
	 * String conditionsOperator;
	 * 
	 * String primaryTable;
	 * 
	 * Map<String,String> primaryTableSecondaryTableMapping;
	 * 
	 * String secondaryTable; String primaryTableColumn;
	 * 
	 * List<String> comparisonValues; String comparisonColumnName;
	 */

	public String query(String db) {
		this.db = db;
		return selectByyCondition(targetTableName, getByIdQuery(subTableNames),
				db);
	}

	public Map<String, Map<String, String>> getSubTableNames() {
		return subTableNames;
	}

	public void setSubTableNames(Map<String, Map<String, String>> subTableNames) {
		this.subTableNames = subTableNames;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	@Override
	public String toString() {
		if (this.db != null && !this.db.isEmpty()) {
			return query(this.db);
		} else {
			return "DB Not Set";
		}
	}

	public <T> String selectByyCondition(String targetTableName,
			Map<String, String> condition, String db) {
		if (db.equalsIgnoreCase("mySQL")) {
			String selectClause = "Select * from " + targetTableName;
			if (condition != null) {
				boolean first = true;
				for (String selectQueries : condition.keySet()) {
					if (first) {
						selectClause += " where " + selectQueries + "_id "
								+ "in (" + condition.get(selectQueries) + ")";
						first = false;
					} else {
						selectClause += " and " + selectQueries + "_id "
								+ "in (" + condition.get(selectQueries) + ")";
					}
				}
			}
			return selectClause;
		} else if (db.equalsIgnoreCase("neo4j")) {
			String selectClause = "start n = node(*) match n-[r:"
					+ targetTableName + "]-n1 ";

			if (condition != null) {
				selectClause = selectClause + "where ";
				boolean first = true;
				for (String conditionString : condition.keySet()) {
					if (conditionString.equalsIgnoreCase("value")) {
						selectClause += (!first ? "and" : "")
								+ " has(r.value) and r.value=\""
								+ condition.get(conditionString) + "\"";
						first = false;
					} else {
						selectClause += (!first ? "and" : "") + " has(n1."
								+ conditionString + ") and n1."
								+ conditionString + "=\""
								+ condition.get(conditionString) + "\"";
						first = false;
					}

				}
				selectClause += " return n;";
			}
			System.out.println(selectClause);
			return selectClause;
		}
		return "";
	}

	public Map<String, String> getByIdQuery(
			Map<String, Map<String, String>> subTableNames) {
		Map<String, String> resultList = null;
		if (db.equalsIgnoreCase("mySQL")) {
			String selectClause = "";
			resultList = new HashMap<String, String>();
			for (String tableClass : subTableNames.keySet()) {
				selectClause = "select id from " + tableClass;
				boolean first = true;
				Map<String, String> innerMap = subTableNames.get(tableClass);
				for (String condtion : innerMap.keySet()) {
					if (first) {
						selectClause += " where " + condtion + "=\""
								+ innerMap.get(condtion) + "\"";
						first = false;
					} else {
						selectClause += " and " + condtion + "=\""
								+ innerMap.get(condtion) + "\"";
					}
				}
				resultList.put(tableClass, selectClause);
			}
		}
		else if (db.equalsIgnoreCase("neo4j")) {
			resultList = new HashMap<String, String>();
			for (String tableClass : subTableNames.keySet()) {
				Map<String, String> innerMap = subTableNames.get(tableClass);
				for (String condtion : innerMap.keySet()) {
					resultList.put(condtion, innerMap.get(condtion));
				}
			}
		}
		return resultList;
	}

}
