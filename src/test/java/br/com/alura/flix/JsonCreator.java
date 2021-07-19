package br.com.alura.flix;

import java.util.List;
import java.util.Objects;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonCreator {

	private StringBuilder stringBuffer;
	private STATUS currentStatus;

	enum STATUS {

		STARTED, NAMED, VALUED, ENDED
	}

	public static JsonCreator startJson() {

		return new JsonCreator(new StringBuilder("{"), STATUS.STARTED);
	}

	public JsonCreator name(String name) {

		if (Objects.equals(STATUS.NAMED, currentStatus)) {
			
			this.stringBuffer.append("null,");
		}

		this.stringBuffer.append(String.format("%s\"%s\":", Objects.equals(STATUS.VALUED, currentStatus) ? "," : "",  name));
		this.currentStatus = STATUS.NAMED;
		return this;
	}

	public JsonCreator value(String value) {

		return setValue(String.format("\"%s\"", value));
	}
	
	public JsonCreator value(Boolean value) {
		
		return setValue(value.toString());
	}
	
	public JsonCreator value(Integer value) {
		return setValue(value + "");
	}
	
	private JsonCreator setValue(String value) {
		
		if (!Objects.equals(STATUS.NAMED, currentStatus)) {

			throw new JsonCreatorException("To define a value the current status should be NAMED");
		}
		
		this.stringBuffer.append(value);
		this.currentStatus = STATUS.VALUED;
		return this;
	}

	public String endJson() {

		if (Objects.equals(STATUS.NAMED, currentStatus)) {
			
			this.stringBuffer.append("null");
		}
		
		this.stringBuffer.append("}");
		return stringBuffer.toString();
	}

	public static class JsonCreatorException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public JsonCreatorException(String message) {

			super(message);
		}
	}

	public static String transform(List<ObjectError> list) {

		JsonCreator json = startJson();
		list.stream().map(FieldError.class::cast).forEach(err -> json.name(err.getField()).value(err.getDefaultMessage()));
		return json.endJson();
	}
}
