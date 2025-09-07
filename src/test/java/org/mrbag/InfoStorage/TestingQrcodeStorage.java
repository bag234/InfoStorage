package org.mrbag.InfoStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingQrcodeStorage {

	final static String DATA_TEST = "Simple DATA TEST for save 1234567890!!@$%^&**(";
	
	@Autowired
	MockMvc mvc;
	
	@Test
	@DisplayName("Correct work with rule")
	public void testQRCodeLiveCycle() throws Exception {
		String keySave = mvc.perform(post("/qrcode/upload?p=SOME_TEST_PASS").content(DATA_TEST))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertNotNull(keySave);
		assertFalse(keySave.isEmpty() && keySave.isEmpty());
		
		assertEquals(DATA_TEST, 
				mvc.perform(get(String.format("/qrcode/%s?p=SOME_TEST_PASS", keySave)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString()
				);
	}
	
	@Test 
	@DisplayName("Test not correct data")
	public void testNotCorrectDataOrMethodInput() throws Exception{
		// Client not set password in request
		mvc.perform(post("/qrcode/upload").content(DATA_TEST)).andExpect(status().is4xxClientError());
		// Client not send date in request
		mvc.perform(post("/qrcode/upload?p=SOME_TEST_PASS")).andExpect(status().is4xxClientError());
		
		mvc.perform(get("/none").content(DATA_TEST)).andExpect(status().is4xxClientError());
		// Checked return or invalide date empty message 
		assertTrue(mvc.perform(get("/qrcode/none?=none")).andReturn().getResponse().getContentAsString().isEmpty());
	}
	
}
