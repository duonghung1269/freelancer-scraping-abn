package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Abn {
	private String abn;
	private String email;
	private String url;

	public Abn() {
	}

	public Abn(String abn) {
		this.abn = abn;
	}
	
	public Abn(String abn, String email, String url) {
		super();
		this.abn = abn;
		this.email = email;
		this.url = url;
	}

	public String getAbn() {
		return abn;
	}

	@XmlElement
	public void setAbn(String abn) {
		this.abn = abn;
	}

	public String getEmail() {
		return email;
	}

	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	@XmlElement
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(abn).append(",")
				 .append(email).append(",")
				 .append("url")
				 .toString();
	}
}
