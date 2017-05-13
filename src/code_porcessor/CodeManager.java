package code_porcessor;

import java.util.ArrayList;

import basic_types.PyClass;
import basic_types.PyEntity;
import basic_types.PyFunction;

public class CodeManager {
	private ArrayList<PyEntity> entities;
	
	public CodeManager(){
		entities = new ArrayList<PyEntity>();
	}
	
	public ArrayList<PyEntity> getEntities(){
		return entities;
	}
	
	public void addEntity(PyEntity e){
		entities.add(e);
	}
	
	public PyEntity getEntity(String name, String type){
		for(PyEntity pe: entities){
			if(pe.getName().equals(name) && pe.getType().equals(type)) return pe;
		}
		return null;
	}
	
	public PyClass getClass(String name){
		for(PyEntity pe: entities){
			if(pe.getName().equals(name) && pe.getType().equals("Class")) return (PyClass) pe;
		}
		return null;
	}
	
	public PyFunction getFunction(String name){
		for(PyEntity pe: entities){
			if(pe.getName().equals(name) && pe.getType().equals("Function")) return (PyFunction) pe;
		}
		return null;
	}
	
	public void setEntity(PyEntity e){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).getName().equals(e.getName())){
				entities.set(i, e);
			}
		}
	}
	
	public void deleteEntity(String name){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).getName().equals(name)){
				entities.remove(i);
			}
		}
	}
}
