package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "text_comment", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false)
//    @JsonIgnore
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ad", referencedColumnName = "id_ad", nullable = false)
//    @JsonIgnore
    private Ad ad;
}
