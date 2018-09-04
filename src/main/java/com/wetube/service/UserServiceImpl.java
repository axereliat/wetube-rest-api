package com.wetube.service;

import com.wetube.domain.entities.Role;
import com.wetube.domain.entities.User;
import com.wetube.domain.models.UserRegisterBindingModel;
import com.wetube.repository.RoleRepository;
import com.wetube.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void register(UserRegisterBindingModel bindingModel) {

        if (this.roleRepository.findAll().size() == 0) {
            Role userRole = new Role();
            userRole.setAuthority("USER");
            Role adminRole = new Role();
            adminRole.setAuthority("ADMIN");

            this.roleRepository.save(userRole);
            this.roleRepository.save(adminRole);
        }

        User user = this.modelMapper.map(bindingModel, User.class);
        user.setPassword(this.passwordEncoder.encode(bindingModel.getPassword()));

        if (this.userRepository.findAll().size() == 0) {
            user.addRole(this.roleRepository.findByAuthority("ADMIN"));
        }
        user.addRole(this.roleRepository.findByAuthority("USER"));

        user.setAvatar("https://res.cloudinary.com/dr8ovbzd2/image/upload/v1534048322/no-user.png");

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void save(User user) {

    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("Wrong username!");
        }

        return user;
    }
}
