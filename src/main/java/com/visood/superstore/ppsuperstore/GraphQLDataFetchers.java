package com.visood.superstore.ppsuperstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.visood.superstore.ppsuperstore.objects.Item;

import graphql.schema.DataFetcher;

@Component
public class GraphQLDataFetchers {
	
	private static final Map<String, List<Item>> orderMap = new ConcurrentHashMap<String, List<Item>>();

    private static List<Map<String, String>> products = Arrays.asList(
            ImmutableMap.of("id", "product-1",
                    "name", "MAC",
                    "mrp", "223",
                    "sp", "210"),
            ImmutableMap.of("id", "product-2",
            		"name", "Parle G",
                    "mrp", "10",
                    "sp", "10"),
            ImmutableMap.of("id", "product-3",
            		"name", "Mouse",
                    "mrp", "120",
                    "sp", "100")
    );

    public DataFetcher getProductIdDataFetcher() {
    	System.out.println("VISHAL :: GraphQLDataFetchers :: getProductIdDataFetcher");
    	
        return dataFetchingEnvironment -> {
            String productId = dataFetchingEnvironment.getArgument("id");
            return products
                    .stream()
                    .filter(product -> product.get("id").equals(productId))
                    .findFirst()
                    .orElse(null);
        };
    }
    
    public DataFetcher getPlaceOrderDataFetcher() {
    	
    	return dataFetchingEnvironment -> {
            ArrayList items = dataFetchingEnvironment.getArgument("items");
            List<Item> itemList = new ArrayList<Item>();
            for(Object obj:items) {
            	if(obj instanceof LinkedHashMap) {
            		LinkedHashMap map = (LinkedHashMap) obj;
            		Item item = new Item((String)map.get("id"), (Integer)map.get("quantity"));
            		itemList.add(item);
            	}
            }
            String orderId = UUID.randomUUID().toString();
            System.out.println("VISHAL :: GraphQLDataFetchers :: getProductIdDataFetcher :: orderId = " + orderId);
			
            orderMap.put(orderId, itemList);
            return orderId;
    	};
    	
    }
}