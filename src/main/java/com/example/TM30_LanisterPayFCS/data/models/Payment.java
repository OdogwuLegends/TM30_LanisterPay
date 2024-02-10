package com.example.TM30_LanisterPayFCS.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity()
public class Payment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String issuer;
    private String brand;
    private String number;
    private String type;
    private String country;
    private String sixId;
}
