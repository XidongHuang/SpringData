package com.tony.springdata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonRepsotory personRepsotory;
	
	@Transactional
	public void savePerson(List<Person> people){
		
		personRepsotory.save(people);
		
	}
	
	@Transactional
	public void updatePersonEmail(String email, Integer id){
		
		personRepsotory.updatePersonEmail(id, email);
		
	}
	
}
