package com.cs.layer2.interactor.manageattribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer2.interactor.intrface.Interactor;
import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ObjectResponse;
import com.cs.layer2.interactor.response.ResponseModel;
import com.cs.layer3.repository.incoming.api.AttributeApi;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;

@Component
public class CreateAttribute implements Interactor {

	@Autowired
	AttributeApi attributeApi;
	
	public ResponseModel execute(RequestModel request) {
		return new ObjectResponse(attributeApi.save((DataTransferObject)request));
	}

	
}
