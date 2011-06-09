package at.ac.tuwien.swag.messages.auth;

import java.io.Serializable;

import at.ac.tuwien.swag.model.dto.UserDTO;

public class StoreUserRequest implements Serializable {
	private static final long serialVersionUID = -3144089580289942553L;

	public StoreUserRequest( UserDTO user ) {
		this.user = user;
	}

	public final UserDTO user;
	
}
