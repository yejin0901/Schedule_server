package com.yj.schedule.domain.schedule;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yj.schedule.domain.comment.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.yj.schedule.domain.comment.QComment.comment;
import static com.yj.schedule.domain.schedule.QSchedule.schedule;


@RequiredArgsConstructor
public class ScheduleRepositoryQueryImpl implements ScheduleRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ScheduleResponseDto> findSchedules(ScheduleSearchCond cond, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cond.getUserId() != null) {
            builder.and(schedule.user.id.eq(cond.getUserId()));
        }
        if (cond.getIsDone() != null) {
            builder.and(schedule.done.eq(cond.getIsDone())); // done 조건은 Boolean으로 처리, 조건에 따라 수정 필요
        }

        List<Schedule> schedules = jpaQueryFactory.selectFrom(schedule)
                .where(builder)
                .leftJoin(schedule.commentList, comment).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(schedule.createdAt.desc())
                .fetch();


        List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());

        long total = jpaQueryFactory.selectFrom(schedule)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(scheduleResponseDtos, pageable, total);
    }

    public List<ScheduleResponseDto> findScheduleProjections(){
        List<ScheduleProjection> result = jpaQueryFactory.selectFrom(schedule)
                .leftJoin(comment).on(schedule.id.eq(comment.schedule.id))
                .where(schedule.done.eq("FALSE"))
                .transform(groupBy(schedule.id).list(Projections.constructor(ScheduleProjection.class,
                        schedule.id,
                        schedule.title,
                        schedule.contents,
                        schedule.done,
                        schedule.createdAt,
                        list(Projections.constructor(CommentResponseDto.class,
                                comment.creator,comment.comments)))));

        return result.stream().map(ScheduleResponseDto::new).toList();
    }

}