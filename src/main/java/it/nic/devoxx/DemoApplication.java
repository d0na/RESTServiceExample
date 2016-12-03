package it.nic.devoxx;

import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.Set;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@RestController
class GreetingController{

	@RequestMapping("/greeting")
	public Person greeting (@RequestParam(value="name", defaultValue="World")String name){
		return new Person();
	}
}

@Entity
@Getter @Setter
@ToString(exclude = "addressList")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
class Person{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String surname;

	@OneToMany(mappedBy = "person")
	private Set<Address> addresses;

	public Person(String n, String s){
		this.name	= n	;
		this.surname = s;
	}
}


@Getter @Setter @ToString(of = {"name","cap"}) @EqualsAndHashCode(of = "id")
@Entity
@NoArgsConstructor
class Address{
	@Id @GeneratedValue
	private Long id;
	private String name;
	private String cap;

	@ManyToOne
	private Person person;

	public Address (String name, String cap,Person person){
		this.name = name;
		this.cap = cap;
		this.person = person;
	}
}

@Service
class DbInitialiser implements CommandLineRunner {

	final PersonRepository persoRepo;
	final AddressRepository addrRepo;

	@Inject
	public DbInitialiser(PersonRepository persoRepo, AddressRepository addrRepo) {
		this.persoRepo = persoRepo;
		this.addrRepo = addrRepo;
	}

	@Override
	public void run(String... strings) throws Exception {

		Person p = new Person("pippo","Franco");
		persoRepo.save(p);
		addrRepo.save(new Address("via moruzzi","561224",p));
		addrRepo.save(new Address("via dovexx","55145",p));


	}
}

@RepositoryRestResource
interface PersonRepository extends PagingAndSortingRepository<Person,Long> {



}

@RepositoryRestResource
interface AddressRepository extends PagingAndSortingRepository<Address,Long> {}