package org.mrbag.InfoStorage.Controller;

import org.mrbag.InfoStorage.Storge.Cloud.Cloud;
import org.mrbag.InfoStorage.Storge.Cloud.CloudKeyAccess;
import org.mrbag.InfoStorage.Storge.Cloud.TypeAccessPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cloud")
public class CloudApi {
 /*
  * Принимает едениц данных для сохранения
  * Возращает ключ для доступа
  * Можно устоновить пароль для доступа к даным
  * Есть возможность ограниченного хранения и записи.
  */
	@Autowired
	Cloud cloud;
	
	@PostMapping("/upload")
	public String saveCloudNote(
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@RequestParam(name = "single", required = false, defaultValue = "no") String canSingle, 
			@RequestParam(name = "type", required = false, defaultValue = "NONE") String type,
			@RequestParam(name = "days", required = false, defaultValue = "365") String days,
			@RequestBody String date
			) {
		TypeAccessPassword t = TypeAccessPassword.NONE;
		if (!password.isEmpty() && type.equals("NONE"))
			t = TypeAccessPassword.ANOTHER;
		else 
			t = TypeAccessPassword.valueOf(type);
		
		return cloud.save(date,password, canSingle.equals("true"), t, days).getKey();
	}
	
	@PatchMapping("/{id}")
	public CloudKeyAccess getInfo(@PathVariable("id") String token) {
		return cloud.checkInfoKey(token);
	}
	
	@GetMapping("/{id}")
	public String getCloudNote(
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@RequestParam(name = "type", required = false, defaultValue = "NONE") String type,
			@PathVariable("id") String token
			) {
		TypeAccessPassword t = TypeAccessPassword.NONE;
		if (!password.isEmpty() && type.equals("NONE"))
			t = TypeAccessPassword.ANOTHER;
		else 
			t = TypeAccessPassword.valueOf(type);
		return cloud.getDataFromAlias(token, password, t);
	}
	
}
