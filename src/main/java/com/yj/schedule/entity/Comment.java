package com.yj.schedule.entity;


import com.yj.schedule.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String comments;

    @Column(nullable = false, length = 200)
    private String creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(CommentRequestDto requestDto, Schedule schedule, User user){
        this.schedule = schedule;
        this.comments = requestDto.getComments();
        this.creator = user.getUsername();
    }

    public void updateComment(CommentRequestDto requestDto){
        this.comments = requestDto.getComments();
    }

}
