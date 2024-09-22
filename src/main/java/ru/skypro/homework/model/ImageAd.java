package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "image_ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image_ad")
    private long id;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "media_type", length = 50)
    private String mediaType;

    @Column(name = "data_image")
//    @JsonIgnore
    private byte[] dataForm;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_ad")
//    @JsonIgnore
    private Ad ad;
}
