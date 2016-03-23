package com.tony.springdata;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;




/**
 * 1. Repository is an empty interface. It is a markable interface
 * 2. If the interface we defined inherited "Repository", then it will be recognized as a "Repository Bean"
 * by IOC container. The the interface can be satisfied some methods
 * 
 * 3. In need, it can use @RepositoryDefinition to inherited Repository interface
 * 
 * 
 * Define methods in Repository sub-interface
 * 1. The methods should follow some rules
 * 2. Querying methods have to start from find| read| get
 * 3. If methods related to querying requirements, the requirements connect with key words of requirements
 * (Capital letter for requirements attribute)
 * 4. Support cascading querying. If the current class has the corresponding attributes, then use them first, but cascade attributes
 * (If want to use cascading attributes, then use "_" to connect)
 */
//@RepositoryDefinition(domainClass=Person.class,idClass=Integer.class)
public interface PersonRepsotory extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person>, PersonDao{

	//Searching the corresponding person by her/his lastName
	Person getByLastName(String lastName);
	
	//WHERE lastName LIKE ?% AND id < ?
	List<Person> getByLastNameStartingWithAndIdLessThan(String lastName, Integer id);
	
	//WHERE lastName LIKE %? AND id < ?
	List<Person> getByLastNameEndingWithAndIdLessThan(String lastName, Integer id);
	
	//WHERE email IN (?,?,?) OR birth < ?
	List<Person> getByEmailInOrBirthLessThan(List<String> emails, Date birth);
	
	//WHERE email IN (?,?,?) AND birth < ?
	List<Person> getByEmailInAndBirthLessThan(List<String> emails, Date birth);
	
	
	//WHERE a.id > ?
	List<Person> getByAddressIdGreaterThan(Integer id);
	
	//Query the MAX(id)
	//Use @Query annotation to self-define JPQL to achieve querying
	@Query("SELECT p FROM Person p WHERE p.id = (SELECT max(p2.id) FROM Person p2)")
	Person getMaxIdPerson();
	
	
	//Use placeHolder to pass argument to method
	@Query("SELECT p FROM Person p WHERE p.lastName = ?1 AND p.email = ?2")
	List<Person> testQueryAnnotationParames1(String lastName, String email);
	
	//Use name argument method to pass argument to method
	@Query("SELECT p FROM Person p WHERE p.lastName = :lastName AND p.email = :email")
	List<Person> testQueryAnnotationParames2(@Param("email") String email, @Param("lastName") String lastName);
	
	//SpringData allows add %% to placeHolders
	@Query("SELECT p FROM Person p WHERE p.lastName LIKE %?1% OR p.email LIKE %?2%")
	List<Person> testQueryAnnotationLikeParam(String lastName, String email);
	
	//SpringData allows add %% to placeHolders
	@Query("SELECT p FROM Person p WHERE p.lastName LIKE %:lastName% OR p.email LIKE %:email%")
	List<Person> testQueryAnnotationLikeParam2(@Param("email") String email, @Param("lastName") String lastName);
	
	//Set nativeQuery=true, then can use SQL
	@Query(value="SELECT count(id) FROM JPA_PERSON", nativeQuery=true)
	long getTotalCount();
	
	
	//Can complete "UPDATE" and "DELETE" by JPQL. Notice: JPQL does not support INSERT
	//Must use @Modifying for @Query.
	//UPDATE and DELETE must in Transactions, and it needs "Service" layer (Use the transaction actions in "Service" layer)
	//In default, SpringData has transaction for each method, but it is just a read-only transaction. It cannot finish modifying action!
	@Modifying
	@Query("UPDATE Person p SET p.email = :email WHERE id = :id")
	void updatePersonEmail(@Param("id") Integer id,@Param("email") String email);
	
}
