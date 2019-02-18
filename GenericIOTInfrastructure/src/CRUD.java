
import java.io.Serializable;

public interface CRUD<T, ID> extends Serializable{	
	ID create(T data);
	T read(ID id);
	void update(ID id, T data);
	void delete(ID id);	
}