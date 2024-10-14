package com.apexglobal.test.orderworker.application.dto;

import com.apexglobal.test.orderworker.domain.model.Order;
import com.apexglobal.test.orderworker.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(
		componentModel = "spring",
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
		)
public
interface ProductMapper {
	@Mapping(source = "id", target = "productId")
	Product toModel(ProductDTO dto);
}
