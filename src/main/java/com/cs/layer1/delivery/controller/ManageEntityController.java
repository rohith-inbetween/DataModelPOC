package com.cs.layer1.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.layer2.interactor.intrface.Interactor;
import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ResponseModel;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;
import com.google.gson.Gson;

@Controller
public class ManageEntityController {

	@Autowired
	public Interactor createEntity;
	@Autowired
	public Interactor updateEntity;
	@Autowired
	public Interactor deleteEntity;
	@Autowired
	public Interactor searchEntity;

	public ManageEntityController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value = "/entity/create", method=RequestMethod.POST)
	public @ResponseBody ResponseModel createEntity(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), EntityDTO.class);
		return createEntity.execute(requestObject);
	}
	
	@RequestMapping(value = "/entity/delete", method=RequestMethod.POST)
	public @ResponseBody ResponseModel deleteEntity(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), EntityDTO.class);
		return deleteEntity.execute(requestObject);
	}
	
	@RequestMapping(value = "/entity/update", method=RequestMethod.POST)
	public @ResponseBody ResponseModel updateEntity(@RequestBody EntityDTO request){
//		System.out.println(request.getBody());
//		RequestModel requestObject = new Gson().fromJson(request.getBody(), EntityDTO.class);
		return updateEntity.execute(request);
	}
	
	@RequestMapping(value = "/entity/search", method=RequestMethod.POST)
	public @ResponseBody ResponseModel searchEntity(HttpEntity<String> request){
		RequestModel requestObject = new Gson().fromJson(request.getBody(), SearchDTO.class);
		return searchEntity.execute(requestObject);
	}
}
