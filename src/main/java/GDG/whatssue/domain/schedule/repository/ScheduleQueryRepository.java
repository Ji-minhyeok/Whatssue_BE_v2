package GDG.whatssue.domain.schedule.repository;

import static GDG.whatssue.domain.schedule.entity.QSchedule.*;

import GDG.whatssue.domain.schedule.dto.SchedulesResponse;
import GDG.whatssue.domain.schedule.dto.SearchCond;
import GDG.whatssue.domain.schedule.entity.Schedule;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryRepository implements ScheduleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public PageImpl<SchedulesResponse> findAllSchedule(Long clubId, SearchCond searchCond, Pageable pageable) {
        System.out.println(searchCond.getQ());
        JPAQuery<SchedulesResponse> query = queryFactory
            .select(Projections.constructor(
                SchedulesResponse.class,
                schedule.id,
                schedule.scheduleName,
                schedule.scheduleDate,
                schedule.scheduleTime))
            .from(schedule)
            .where(
                filterClub(clubId),
                filterQuery(searchCond.getQ()),
                filterDate(searchCond.getSDate(), searchCond.getEDate()))
            .orderBy(schedule.scheduleDate.asc(), schedule.scheduleTime.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

//        for (Sort.Order o : pageable.getSort()) {
//            PathBuilder pathBuilder = new PathBuilder(schedule.getType(), schedule.getMetadata());
//            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//                pathBuilder.get(o.getProperty())));
//        }

        List<SchedulesResponse> results = query.fetch();

        long total = query.select(schedule)
            .from(schedule)
            .where(
                filterClub(clubId),
                filterQuery(searchCond.getQ()),
                filterDate(searchCond.getSDate(), searchCond.getEDate()))
            .stream()
            .count();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression filterClub(Long clubId) {
        return schedule.club.id.eq(clubId);
    }

    private BooleanExpression filterQuery(String searchQuery) {
        if (StringUtils.hasText(searchQuery)) {
            return schedule.scheduleName.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression filterDate(String sDate, String eDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(sDate, formatter);
        LocalDate endDate = LocalDate.parse(eDate, formatter);

        return schedule.scheduleDate.between(startDate, endDate);
    }
}
