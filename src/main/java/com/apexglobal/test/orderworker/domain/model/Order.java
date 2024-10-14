package com.apexglobal.test.orderworker.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document(collection = "orders")
@CompoundIndex(name = "orderId_1", def = "{'orderId': 1}")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	private String id;
	protected String orderId;
	protected String customerId;
	private List<Product> products;
}
