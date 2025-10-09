package com.ecommerce.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "admins")
@NoArgsConstructor
@Data
public class Admin extends User {
}
