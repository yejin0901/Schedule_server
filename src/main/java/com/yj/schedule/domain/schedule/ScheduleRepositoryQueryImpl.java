package com.yj.schedule.domain.schedule;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yj.schedule.domain.comment.QComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.yj.schedule.domain.comment.QComment.comment;
import static com.yj.schedule.domain.schedule.QSchedule.schedule;


@RequiredArgsConstructor
public class ScheduleRepositoryQueryImpl implements ScheduleRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ScheduleResponseDto> getUserSchedule(ScheduleSearchCond cond, Pageable pageable){
        JPAQuery<Schedule> query = getScheduleQuery(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        query.orderBy(schedule.createdAt.desc());

        List<Schedule> schedules = query.fetch();

        List<ScheduleResponseDto> scheduleResponseDtos = schedules.stream()
                .map(ScheduleResponseDto::new)
                .toList();

        return new PageImpl<>(scheduleResponseDtos);
    }

    private JPAQuery<Schedule> getScheduleQuery(ScheduleSearchCond cond){
        return jpaQueryFactory.selectFrom(schedule)
                .leftJoin(schedule.commentList, comment).fetchJoin()
                .where(schedule.user.id.eq(cond.getUserId()));

    }
}
