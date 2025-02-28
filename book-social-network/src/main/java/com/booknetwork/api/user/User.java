package com.booknetwork.api.user;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.booknetwork.api.book.Book;
import com.booknetwork.api.history.BookTransactionHistory;
import com.booknetwork.api.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	@Column(unique = true)
	private String email;
	private String password;
	private boolean accountLocked;
	private boolean accountEnabled;

	// roles
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	// books
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
	private List<Book> books;

	// transactions
	@OneToMany(mappedBy = "user")
	private List<BookTransactionHistory> bookTransactions;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdTime;
	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedTime;

	@Override
	public String getName() {
		return this.email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.accountEnabled;
	}

	public String getFullName() {
		return String.format("%s %s", this.firstName, this.lastName);
	}

}
