package org.mrbag.InfoStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mrbag.InfoStorage.Storge.Cloud.CloudKeyAccess;
import org.mrbag.InfoStorage.Storge.Cloud.TypeAccessPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingCloudStorage {

final static String DATA_TEST = "Simple DATA TEST for save 1234567890!!@$%^&**(";

	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper map;
	
	@Test
	@DisplayName("All of not contain key")
	public void testWrongDelete() throws Exception {
		mvc.perform(delete("/cloud/none")).andExpect(status().isNotFound());
		mvc.perform(get("/cloud/none")).andExpect(status().isBadRequest());
		mvc.perform(patch("/cloud/none")).andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Wrong uploud Data")
	public void testWrongUploud() throws Exception {
		mvc.perform(post("/cloud/upload")).andExpect(status().isBadRequest());
		mvc.perform(post("/cloud/upload?days=-2").content(DATA_TEST))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Life cycle (Simple)")
	public void testLifeCycleRequstSimplie() throws Exception {
		String sm_key = mvc.perform(post("/cloud/upload").content(DATA_TEST))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		CloudKeyAccess ckey = map.readValue(
				mvc.perform(patch("/cloud/" +sm_key)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				, CloudKeyAccess.class);
		
		assertTrue(!ckey.isSingle() 
				&& ckey.getType() == TypeAccessPassword.NONE  
				&& ckey.getTime().isAfter(LocalDateTime.now()));
		
		assertEquals(DATA_TEST, 
				mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
				);
		mvc.perform(delete("/cloud/" + sm_key)).andExpect(status().isOk());
		mvc.perform(delete("/cloud/" + sm_key)).andExpect(status().isNotFound());
		mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	@DisplayName("Life cycle (Password)")
	public void testLifeCycleRequstPassword() throws Exception {
		String sm_key = mvc.perform(post("/cloud/upload?password=Some_test").content(DATA_TEST))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		CloudKeyAccess ckey = map.readValue(
				mvc.perform(patch("/cloud/" +sm_key)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				, CloudKeyAccess.class);
		
		assertTrue(!ckey.isSingle() 
				&& ckey.getType() == TypeAccessPassword.ANOTHER  
				&& ckey.getTime().isAfter(LocalDateTime.now()));
		
		assertEquals(DATA_TEST, 
				mvc.perform(get("/cloud/" + sm_key + "?password=Some_test"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				);
		mvc.perform(delete("/cloud/" + sm_key + "?password=Some_test")).andExpect(status().isOk());
		mvc.perform(delete("/cloud/" + sm_key + "?password=Some_test")).andExpect(status().isNotFound());
		mvc.perform(get("/cloud/" + sm_key + "?password=Some_test")).andExpect(status().isBadRequest());
		
	}
	
	@Test
	@DisplayName("Life cycle (PIN)")
	public void testLifeCycleRequstPIN() throws Exception {
		String sm_key = mvc.perform(post("/cloud/upload?password=Some_test&type=PIN").content(DATA_TEST))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		CloudKeyAccess ckey = map.readValue(
				mvc.perform(patch("/cloud/" +sm_key)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				, CloudKeyAccess.class);
		
		assertTrue(!ckey.isSingle() 
				&& ckey.getType() == TypeAccessPassword.ANOTHER  
				&& ckey.getTime().isAfter(LocalDateTime.now()));
		
		assertEquals(DATA_TEST, 
				mvc.perform(get("/cloud/" + sm_key + "?password=Some_test&type=PIN"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				);
		mvc.perform(delete("/cloud/" + sm_key + "?password=Some_test&type=PIN")).andExpect(status().isOk());
		mvc.perform(delete("/cloud/" + sm_key + "?password=Some_test&type=PIN")).andExpect(status().isNotFound());
		mvc.perform(get("/cloud/" + sm_key + "?password=Some_test&type=PIN")).andExpect(status().isBadRequest());
		
	}
	
	@Test
	@DisplayName("Life cycle (Days)")
	public void testLifeCycleRequstSimplieDays() throws Exception {
		String sm_key = mvc.perform(post("/cloud/upload?days=5").content(DATA_TEST))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		
		CloudKeyAccess ckey = map.readValue(
				mvc.perform(patch("/cloud/" +sm_key)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				, CloudKeyAccess.class);
		
		assertTrue(!ckey.isSingle() 
				&& ckey.getType() == TypeAccessPassword.NONE  
				&& ckey.getTime().isAfter(LocalDateTime.now()));
		
		assertTrue(ckey.getTime().plusDays(6).isAfter(LocalDateTime.now()));
		
		assertEquals(DATA_TEST, 
				mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
				);
		mvc.perform(delete("/cloud/" + sm_key)).andExpect(status().isOk());
		mvc.perform(delete("/cloud/" + sm_key)).andExpect(status().isNotFound());
		mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	@DisplayName("Life cycle (Single)")
	public void testLifeCycleRequstSimplieSinffle() throws Exception {
		String sm_key = mvc.perform(post("/cloud/upload?single=true").content(DATA_TEST))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString(); 
		
		CloudKeyAccess ckey = map.readValue(
				mvc.perform(patch("/cloud/" +sm_key)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				, CloudKeyAccess.class);
		
		assertTrue(!ckey.isSingle() 
				&& ckey.getType() == TypeAccessPassword.NONE  
				&& ckey.getTime().isAfter(LocalDateTime.now()));
		
		assertEquals(DATA_TEST, 
				mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString()
				);
		
		mvc.perform(get("/cloud/" + sm_key)).andExpect(status().isBadRequest());
		
	}
	
}
