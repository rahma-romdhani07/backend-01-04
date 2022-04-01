package com.projet.stock.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Entity
@Table(name = "Utilisateur",
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"
				+ ""),
		@UniqueConstraint(columnNames = "email") 
	})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="roles")
public abstract class User {
	
	@Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	  @NotBlank
		@Size(max = 100)
	  protected String username;
	  @NotBlank
	  @Size(max = 100)
	  @Email
	  protected String email;
	  protected String password;
	  @Column(name = "image", length = 1000)
	  protected byte[] image;
	
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email  + ", username=" + username + ", password="+ password + "]";
				
	}
	public User(String username, String email, String password  , byte[] image  ) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
	
	}
	
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public User() {
		super();
		
	}

}