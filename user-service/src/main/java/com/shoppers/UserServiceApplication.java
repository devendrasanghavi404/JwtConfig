package com.shoppers;

import com.shoppers.model.Role;
import com.shoppers.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	@Value("${admin.role.id}")
	private String role_admin_id;
	@Value("${normal.role.id}")
	private String role_normal_id;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
//			role_admin = Role.builder().roleId(UUID.randomUUID().toString()).roleName("ROLE_ADMIN").build();
			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.save(role_admin);
			roleRepository.save(role_normal);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
