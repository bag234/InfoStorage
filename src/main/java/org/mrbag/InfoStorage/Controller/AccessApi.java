package org.mrbag.InfoStorage.Controller;

import org.mrbag.InfoStorage.Storge.KeyStore;
import org.mrbag.InfoStorage.Storge.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qrcode")
public class AccessApi {

	@Autowired
	Store store;

	@PostMapping("/upload")
	@ResponseBody
	public String uploadDataAPI(
			@RequestParam(name = "p") String password,
			@RequestBody String data) {
		return store.save(password, data).getId();
	}

	@GetMapping("/{id}")
	@ResponseBody
	public String getDataAPI(
			@RequestParam(name = "p") String password,
			@PathVariable(name = "id") String id) {

		return store.load(new KeyStore(password, id));
	}

}