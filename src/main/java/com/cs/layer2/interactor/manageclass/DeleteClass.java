package com.cs.layer2.interactor.manageclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer2.interactor.intrface.Interactor;
import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ResponseModel;
import com.cs.layer3.repository.incoming.api.ClazzApi;
import com.cs.layer3.repository.incoming.dto.ClassDTO;

@Component
public class DeleteClass implements Interactor {

	
	@Autowired
	ClazzApi clazzApi;

	public ResponseModel execute(RequestModel request) {
		ClassDTO classDTO = (ClassDTO)request;
		clazzApi.delete(classDTO.getId());
		return null;
	}
	
	
	
}
