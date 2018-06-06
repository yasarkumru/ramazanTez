package tr.com.metu.ramazan.model;

public class Station {
	private Integer id;
	
	public Station (Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	
	@Override
	public String toString() {
		return "[Station id: "+ id+ "]";
	}


}
