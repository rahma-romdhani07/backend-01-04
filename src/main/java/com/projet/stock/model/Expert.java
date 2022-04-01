package com.projet.stock.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="Expert")

public class Expert extends User {
	protected String gender ;
	protected long telephone ;
	
	public Expert( String username, String email, String password, String gender, long telephone , byte[] image  ) {
		super(username,email,password,image);
		this.gender=gender;
		this.telephone=telephone;
	
	}
	public Expert(byte[]image) {
		this.image=image ;
	}
	public Expert() {
		super();
	}
	
	 public String getGender() {
			return gender;
		}
		
		public void setGender(String gender) {
			this.gender = gender;
		}

	
		public long getTelephone() {
			return telephone;
		}

		public void setTelephone(long telephone) {
			this.telephone = telephone;
		}

		public String getUsername(){
			return super.getUsername();
		}
		

		public void setUsername(){
			 super.setUsername(super.getUsername());
			 
		}
		public String getEmail(){
			return super.getEmail();
		}
		

		public void setEmail(){
			 super.setEmail(super.getEmail());
		}
		
		
		public String getPassword(){
			return super.getPassword();
		}
		
		public void setPassword(){
			super.setPassword(super.getPassword());
		}	

		public void setImage(){
			 super.setImage(super.getImage());
		}
		public byte[] getImage(){
			return super.getImage();
		}
		

	

@Override
	public String toString() {
		return "Expert [gender=" + gender + ", telephone=" + telephone + ", id=" + id + ", username=" + username
				+ ", email=" + email + ", password=" + password + ", image=" + image + "]";
	}
}
