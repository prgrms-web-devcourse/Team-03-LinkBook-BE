package com.prgrms.team03linkbookbe.tag.entity;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private TagCategory name;

    @OneToMany(mappedBy = "tag")
    private List<Folder> folders = new ArrayList<>();


    @Builder
    public Tag(Long id, TagCategory name,
        List<Folder> folders) {
        this.id = id;
        this.name = name;
    }
}
