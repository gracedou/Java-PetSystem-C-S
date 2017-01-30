//*********************************************************************
//*                                                                   *
//* CIS611               Summer Session 2016                Bing Dou  *
//*                                                                   *
//*                    Program Assignment PA06                        * 
//*                                                                   *
//*                       Class Description                           *
//*                                                                   *
//*                                                                   *
//*           	           7/2/2016 Created                           *
//*                                                                   *
//*                     Saved in: Message.java                        *
//*                                                                   *
//*********************************************************************
//

import java.io.Serializable;
import java.util.Date;
import java.net.*;

public class Message  implements Serializable {
	
	public Integer id;
	public String name;
	public String ownerFName;
	public String ownerLName;
	public char sex;
	public String species;
	public String color;
	public Date birth;
	public int opType;
	
	public Message(int id, String name, String ownerFName, String ownerLName, char sex, String species,
			String color, Date birth, int opType) {
		this.id = id;
		this.name = name;
		this.ownerFName = ownerFName;
		this.ownerLName = ownerLName;
		this.sex = sex;
		this.species = species;
		this.color = color;
		this.birth = birth;
		this.opType = opType;
	}

}
