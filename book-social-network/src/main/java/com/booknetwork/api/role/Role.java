package com.booknetwork.api.role;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.booknetwork.api.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnore
	private Set<User> users;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdTime;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedTime;
}
