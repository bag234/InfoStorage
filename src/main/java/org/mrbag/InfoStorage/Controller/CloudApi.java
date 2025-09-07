package org.mrbag.InfoStorage.Controller;

import org.mrbag.InfoStorage.Storge.Cloud.Cloud;
import org.mrbag.InfoStorage.Storge.Cloud.TypeAccessPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	private static TypeAccessPassword simpleParsingType(boolean canPass, String type) {
		TypeAccessPassword t = TypeAccessPassword.NONE;
		if (canPass && type.equals("NONE"))
			t = TypeAccessPassword.ANOTHER;
		else 
			t = TypeAccessPassword.valueOf(type.toUpperCase());
		return t;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> saveCloudNote(
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@RequestParam(name = "single", required = false, defaultValue = "false") boolean canSingle, 
			@RequestParam(name = "type", required = false, defaultValue = "NONE") String type,
			@RequestParam(name = "days", required = false, defaultValue = "365") int days,
			@RequestBody String date
			) {
		try {
			return ResponseEntity.ok(cloud.save(date,password, canSingle, simpleParsingType(!password.isEmpty(), type), days).getKey());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> getInfo(@PathVariable("id") String token) {
		try {
			return ResponseEntity.ok( cloud.checkInfoKey(token));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getCloudNote(
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@RequestParam(name = "type", required = false, defaultValue = "NONE") String type,
			@PathVariable("id") String token
			) {
		
		try {
			return ResponseEntity.ok(cloud.getDataFromAlias(token, password, simpleParsingType(!password.isEmpty(), type)));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCloudNote(
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@RequestParam(name = "type", required = false, defaultValue = "NONE") String type,
			@PathVariable("id") String token
			) {
		
		if (cloud.delete(token, password, simpleParsingType(!password.isEmpty(), type)))
			return ResponseEntity.ok().build();
		else 
			return ResponseEntity.notFound().build();
	}

}
