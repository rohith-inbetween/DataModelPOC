package com.cs.layer3.repository.business.defalt.interfaces;

import java.util.Map;
import java.util.Set;

import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;

public interface IEntityStrategy {

	public EntityDTO save(EntityDTO bObject);
	
	public void delete(Long entityId);

	public EntityDTO getById(Long id);

	public DataTransferObject getEntityMapping(Long id);
	
	public EntityDTO update(EntityDTO entityToUpdate);
	
	Set<EntityDTO> search(SearchDTO searchCriteria);
	
	public void setParent(Long parentId, Long childId);
}
