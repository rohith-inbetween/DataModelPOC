package com.cs.layer3.repository.incoming.api;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.layer3.repository.business.defalt.interfaces.IEntityStrategy;
import com.cs.layer3.repository.incoming.dto.DataTransferObject;
import com.cs.layer3.repository.incoming.dto.EntityDTO;
import com.cs.layer3.repository.incoming.dto.SearchDTO;

@Component
public class EntityApi {

	@Autowired
	IEntityStrategy entityStrategy;

	public DataTransferObject save(DataTransferObject entityBO) {
		return entityStrategy.save((EntityDTO) entityBO);
	}

	public DataTransferObject findOne(Long id) {
		return entityStrategy.getById(id);
	}

	public DataTransferObject getMapppingForGivenEntity(Long id) {
		return entityStrategy.getEntityMapping(id);
	}

	public void deleteEntity(Long id) {
		entityStrategy.delete(id);
	}

	public Set searchValues(SearchDTO searchParams) {
		return entityStrategy.search(searchParams);
	}

	public DataTransferObject update(EntityDTO entityToUpdate) {
		return entityStrategy.update(entityToUpdate);
	}

}
