package com.cs.layer1.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.layer2.interactor.intrface.Interactor;
import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ResponseModel;
import com.cs.layer3.repository.incoming.dto.ClassDTO;
import com.google.gson.Gson;

@Controller
public class ManageClassController {

	@Autowired
	public Interactor createClass;
	@Autowired
	public Interactor updateClass;
	@Autowired
	public Interactor deleteClass;

	public ManageClassController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/class/create", method=RequestMethod.POST)
	public @ResponseBody ResponseModel createClass(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), ClassDTO.class);
		return createClass.execute(requestObject);
	}
	
	@RequestMapping(value = "/class/delete", method=RequestMethod.POST)
	public @ResponseBody ResponseModel deleteClass(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), ClassDTO.class);
		return deleteClass.execute(requestObject);
	}
	
	@RequestMapping(value = "/class/update", method=RequestMethod.POST)
	public @ResponseBody ResponseModel updateClass(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), ClassDTO.class);
		return updateClass.execute(requestObject);
	}
}
