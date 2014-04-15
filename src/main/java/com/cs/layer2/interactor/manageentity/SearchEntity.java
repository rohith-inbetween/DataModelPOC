package com.cs.layer2.interactor.manageentity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer2.interactor.intrface.Interactor;
import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ObjectResponse;
import com.cs.layer2.interactor.response.ResponseModel;
import com.cs.layer3.repository.incoming.api.EntityApi;
import com.cs.layer3.repository.incoming.dto.SearchDTO;

@Component
public class SearchEntity implements Interactor {
	
	
	@Autowired
	EntityApi entityApi;

	public ResponseModel execute(RequestModel request) {
		return new ObjectResponse(entityApi.searchValues((SearchDTO) request));
	}

}
