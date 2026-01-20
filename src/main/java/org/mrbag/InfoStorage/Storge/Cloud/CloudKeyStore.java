package org.mrbag.InfoStorage.Storge.Cloud;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Специфичный объет для создания облочных записей, для хранилища контактов,
 * Н-ситемы
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudKeyStore {

	String key;

	@JsonIgnore
	@Builder.Default
	String password = "";

	@Builder.Default
	boolean isSingle = false;

	@Builder.Default
	TypeAccessPassword type = TypeAccessPassword.NONE;

	@Builder.Default
	LocalDateTime time = LocalDateTime.now();

}
