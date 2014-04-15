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
import com.cs.layer3.repository.incoming.dto.AttributeDTO;
import com.google.gson.Gson;

@Controller
public class ManageAttributeController {

	@Autowired
	public Interactor createAttribute;
	@Autowired
	public Interactor updateAttribute;
	@Autowired
	public Interactor deleteAttribute;

	public ManageAttributeController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/attribute/create", method=RequestMethod.POST)
	public @ResponseBody ResponseModel createAttribute(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), AttributeDTO.class);
		return createAttribute.execute(requestObject);
	}
	
	@RequestMapping(value = "/attribute/delete", method=RequestMethod.POST)
	public @ResponseBody ResponseModel deleteAttribute(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), AttributeDTO.class);
		return deleteAttribute.execute(requestObject);
	}
	
	@RequestMapping(value = "/attribute/update", method=RequestMethod.POST)
	public @ResponseBody ResponseModel updateAttribute(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), AttributeDTO.class);
		return updateAttribute.execute(requestObject);
	}
}
