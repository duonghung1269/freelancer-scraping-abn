package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbnCollection {
	
	private List<Abn> abns = new ArrayList<>();

	@XmlElement(name="abns")
	public List<Abn> getAbns() {
		return abns;
	}

	public void setAbns(List<Abn> abns) {
		this.abns = abns;
	}
	
	
}
