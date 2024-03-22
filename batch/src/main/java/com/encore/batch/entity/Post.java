package com.encore.batch.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

    protected Post() {

    }
    public void updatePost(String title, String contents){
        this.title = title;
        this.contents = contents;
    }

    public void deletePost(){ // item 삭제 시 호출
        this.delYn = "Y";
    }

//    public void updateAppointment(String appointment){
//        this.appointment = appointment;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 3000)
    private String contents;

//    author_id는 DB의 컬럼명, 별다른 옵션없을시 author의 pk에 fk가 설정
//    post객체 입장에서는 한사람이 여러개 글을 쓸수 있으므로 N:1 ManyToOne으로 설정

    private String email;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

//    private String appointment;
//    private LocalDateTime appointmentTime;

    @Builder.Default // Builder.Default 를 붙혀주지 않으면 Builder에 기본 null로 세팅되어 있기 때문에 db에 null이 들어간다.
    private String delYn="N"; // item 삭제 유무

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comment = new ArrayList<>();


    //    factory Method
    public Post(String title, String contents, String email) {
        this.title = title;
        this.contents = contents;
        this.email = email;
    }
    public static Post CreatePost(String title, String contents, String email) {
        return new Post(title, contents, email);
    }



}
