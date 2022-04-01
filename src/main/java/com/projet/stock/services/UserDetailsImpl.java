package com.projet.stock.services;

import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.projet.stock.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String email;
	//private String gender ; 
	//private long telephone ; 
	private String image ;
	private String role;
	@JsonIgnore
	private String password;
	
	  private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String email, String password
			/*,String gender , String image , long telephone*/ ) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		//this.role     = role;
	}

	public static UserDetailsImpl build(User user) {
		
		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword()
				
				/*user.getGender(),
				user.getTelephone(),
				user.getImage()*/
				);
	}

	

	public Long getId() {
		return id;
	}

	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
/*	public String getGender() {
		return gender;
	}*/
	/*public void setGender(String gender) {
		this.gender = gender;
	}*/
	public String getImage() {
		return image;
	}
	//public void setTelephone(long telephone) {
		//this.telephone = telephone;
	//}
	public String getEmail() {
		return email;
	}
	public void setImage(String image) {
		this.image = image;
	}
//	public long getTelephone() {
	//	return telephone;
	//}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
