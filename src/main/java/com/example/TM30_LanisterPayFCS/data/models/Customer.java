package com.example.TM30_LanisterPayFCS.data.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {
    private Long id;
    private String fullName;
    private String emailAddress;
    private boolean bearsFee;
}
