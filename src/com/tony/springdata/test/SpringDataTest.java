package com.tony.springdata.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.tony.springdata.Person;
import com.tony.springdata.PersonRepsotory;
import com.tony.springdata.PersonService;

public class SpringDataTest {

	private ApplicationContext ctx = null;
	private PersonRepsotory personRepsotory = null;
	private PersonService personService;

	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		personRepsotory = ctx.getBean(PersonRepsotory.class);
		personService = ctx.getBean(PersonService.class);

	}

	@Test
	public void testCustomerRepositoryMethod(){
		
		personRepsotory.test();
		
	}
	
	
	/**
	 * Page with sorting
	 * 
	 * 
	 */
	@Test
	public void testJpaSpecificationExecutor() {

		int pageNo = 3 - 1;
		int pageSize = 5;

		PageRequest pageRequest = new PageRequest(pageNo, pageSize);
		// Usually use Specification's inner class
		Specification<Person> specification = new Specification<Person>() {

			/**
			 * @param root: stands for querying instance class
			 * @param query:
			 * @param cb: CriteriaBuilder, the factory for building instances that relate with CriteriaBuilder,
			 * and can gain Predicate object
			 * @return: Predicate type, it stands for a query requirement  
			 */
			@Override
			public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				Path path = root.get("id");
				
				Predicate predicate = cb.gt(path,5);
				
				return predicate;
			}
		};

		Page<Person> page = personRepsotory.findAll(specification, pageRequest);
		
		System.out.println("Total Elements: " + page.getTotalElements());
		System.out.println("Current page number: " + (page.getNumber() + 1));
		System.out.println("Total page number: " + page.getTotalPages());
		System.out.println("Current page list: " + page.getContent());
		System.out.println("Current page log: " + page.getNumberOfElements());

	}

	@Test
	public void testJpaRepository() {
		Person person = new Person();
		person.setBirth(new Date());
		person.setEmail("XYZ@gmail.com");
		person.setLastName("xyz");

		personRepsotory.saveAndFlush(person);

	}

	@Test
	public void testPagingAndSortingRespository() {
		int pageNo = 4 - 1;
		int pageSize = 5;

		// Sort
		Order order1 = new Order(Direction.DESC, "id");
		Order order2 = new Order(Direction.ASC, "email");
		Sort sort = new Sort(order1, order2);

		PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);

		Page<Person> page = personRepsotory.findAll(pageRequest);

		System.out.println("Total Elements: " + page.getTotalElements());
		System.out.println("Current page number: " + (page.getNumber() + 1));
		System.out.println("Total page number: " + page.getTotalPages());
		System.out.println("Current page list: " + page.getContent());
		System.out.println("Current page log: " + page.getNumberOfElements());

	}

	@Test
	public void testCrudRepository() {

		List<Person> people = new ArrayList<>();

		for (int i = 'a'; i <= 'z'; i++) {
			Person person = new Person();
			person.setBirth(new Date());
			person.setEmail((char) i + "" + (char) i + "gmail.com");
			person.setLastName((char) i + "" + (char) i);

			people.add(person);

		}
		personService.savePerson(people);
	}

	@Test
	public void testModifying() {

		// personRepsotory.updatePersonEmail(1, "mmm@gmail.com");
		personService.updatePersonEmail("mmm@gmail.com", 1);

	}

	@Test
	public void testNativeQuery() {
		long count = personRepsotory.getTotalCount();

		System.out.println(count);

	}

	@Test
	public void testQueryAnnotationLikeParam() {

		// List<Person> people =
		// personRepsotory.testQueryAnnotationLikeParam("A", "bb");
		// System.out.println(people.size());

		List<Person> people = personRepsotory.testQueryAnnotationLikeParam2("bb", "A");
		System.out.println(people.size());

	}

	@Test
	public void testQueryAnnotationParams2() {

		List<Person> people = personRepsotory.testQueryAnnotationParames2("aa@gmail.com", "AA");
		System.out.println(people);

	}

	@Test
	public void testQueryAnnotationParams1() {

		List<Person> people = personRepsotory.testQueryAnnotationParames1("AA", "aa@gmail.com");
		System.out.println(people);

	}

	@Test
	public void testQueryAnnotation() {
		Person person = personRepsotory.getMaxIdPerson();

		System.out.println(person);

	}

	@Test
	public void testKeyWords() {

		List<Person> people = personRepsotory.getByLastNameStartingWithAndIdLessThan("X", 10);

		System.out.println(people);
		people = personRepsotory.getByLastNameEndingWithAndIdLessThan("X", 10);

		System.out.println(people);

		people = personRepsotory
				.getByEmailInOrBirthLessThan(Arrays.asList("aa@gmail.com", "ff@gmail.com", "ss@gmail.com"), new Date());
		System.out.println(people.size());

		people = personRepsotory.getByEmailInAndBirthLessThan(
				Arrays.asList("aa@gmail.com", "ff@gmail.com", "ss@gmail.com"), new Date());
		System.out.println(people.size());

	}

	@Test
	public void testKeyWords2() {

		List<Person> people = personRepsotory.getByAddressIdGreaterThan(1);
		System.out.println(people);

	}

	@Test
	public void tHelloWorldSpringData() {

		System.out.println(personRepsotory.getClass().getName());

		Person person = personRepsotory.getByLastName("AA");

		System.out.println(person);

	}

	@Test
	public void testJpa() {

	}

	@Test
	public void testDataSource() throws SQLException {

		DataSource dataSource = ctx.getBean(DataSource.class);

		System.out.println(dataSource.getConnection());

	}

}
