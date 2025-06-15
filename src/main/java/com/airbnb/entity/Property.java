package com.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private double pricePerNight;
    @Column(name = "max_guests", nullable = false)
    private int maxGuests;


    @ManyToOne
    @JoinColumn(name = "host_id") // optional: you can use 'user_id' too
    private User host;

    private String imageUrl;
    
   

}
