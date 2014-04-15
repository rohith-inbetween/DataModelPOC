package com.cs.layer3.repository.incoming.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.interfaces.IClazzStrategy;
import com.cs.layer3.repository.incoming.dto.ClassDTO;

@Component
public class ClazzApi{

	@Autowired
	private IClazzStrategy clazzStrategy;
	
	public ClassDTO save(ClassDTO classBO){
		return clazzStrategy.save(classBO);
	}
	
	public ClassDTO findOne(Long id){
		return clazzStrategy.getById(id);
	}
	
	public ClassDTO getClazzAttributeMapping(Long clazzId) {
		return clazzStrategy
				.getClazzAttributeMappingByClazzId(clazzId);
	}
	
	public ClassDTO update(ClassDTO Class)
	{
		return clazzStrategy.update(Class);
	}
	
	public void delete(Long id)
	{
		clazzStrategy.delete(id);
	}
}
