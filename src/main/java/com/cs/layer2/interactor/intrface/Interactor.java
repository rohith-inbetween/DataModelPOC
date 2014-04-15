package com.cs.layer2.interactor.intrface;

import com.cs.layer2.interactor.request.RequestModel;
import com.cs.layer2.interactor.response.ResponseModel;

public interface Interactor {

	public ResponseModel execute(RequestModel request);
	
}
