
package entitybeans;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity(name = "Pokemon")
@Table(name = "POKEMON")
@Access(AccessType.PROPERTY)
@NamedQueries({
        @NamedQuery(name = "getPokemons",
                    query = "SELECT p FROM Pokemon p"),
        @NamedQuery(name = "getPokemonByName",
                    query = "SELECT p FROM Pokemon p WHERE p.name = :name"),
        @NamedQuery(name = "getPokemonByType",
        query = "SELECT p FROM Pokemon p WHERE p.type = :type")
})
public class Pokemon {

	/*
	  	 ID: varchar
  		 Name: varchar
  		 Type: varchar
  		 arge: int
	  
	  */
	
    private String id;

    private String name;

    private String type;
    
    private int age;

    public Pokemon() {
    }

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     
    @Column(name = "Type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Age", nullable = false)
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
