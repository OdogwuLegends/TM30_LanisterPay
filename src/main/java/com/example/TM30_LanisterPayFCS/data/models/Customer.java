package com.example.TM30_LanisterPayFCS.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Customer {
    @Id
    private Long id;
    private String fullName;
    private String emailAddress;
    private boolean bearsFee;
}
