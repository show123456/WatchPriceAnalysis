package daoconnection;

import java.io.Serializable;

public class Type implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String type;

	// Getters / Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// Object overrides
	@Override
	public boolean equals(Object other) {
		return (other instanceof Type) && this.id == ((Type) other).id;
	}

	@Override
	public int hashCode() {
		Integer id = this.id;
		return this.getClass().hashCode() + id.hashCode();
	}

	@Override
	public String toString() {
		return String.format("Type[id=%d, type=%s]", this.id, this.type);
	}
}
