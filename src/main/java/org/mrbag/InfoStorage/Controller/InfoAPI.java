package org.mrbag.InfoStorage.Controller;

import org.mrbag.InfoStorage.Storge.Cloud.TypeAccessPassword;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoAPI {

	@GetMapping("/password")
	public Enum<?>[] getPasswordType(){
		return TypeAccessPassword.values();
	}
	
}
