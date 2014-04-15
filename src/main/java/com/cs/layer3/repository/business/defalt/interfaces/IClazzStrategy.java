package com.cs.layer3.repository.business.defalt.interfaces;

import com.cs.layer3.repository.incoming.dto.ClassDTO;

public interface IClazzStrategy {

	public ClassDTO save(ClassDTO bObject);
	
	public void delete(Long id);

	public ClassDTO getById(Long id);
	
	public ClassDTO update(ClassDTO updatedClass) ;
		

	ClassDTO getClazzAttributeMappingByClazzId(Long id);	
}
