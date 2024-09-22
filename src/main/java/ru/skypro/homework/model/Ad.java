package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ad")
    private long id;

    @Column(name = "text_title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "text_description", nullable = false)
    private String description;

    @OneToOne(mappedBy = "ad")
//    @JsonIgnore
    private ImageAd imageAd;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
//    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "ad")
//    @JsonIgnore
    private List<Comment> comments;
}
