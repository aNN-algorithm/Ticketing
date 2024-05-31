package zerobase.ticketing.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.Review;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReviewJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewJDBCRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public RowMapper<Review> popularShowReviewRowMapper() {
        return (rs, rowNum) -> new Review(
                rs.getInt("id"),
                rs.getInt("booking_id"),
                rs.getString("user_id"),
                rs.getDate("register_date").toLocalDate(),
                rs.getString("text"),
                rs.getInt("show_id"),
                rs.getString("show_name"),
                rs.getInt("show_info_id")
        );
    }

    public List<Review> readPopularShowReview(String genre, LocalDate startDate, LocalDate endDate) {
        String sql = "select id, user_id, register_date, text, show_info_id " +
                "from review " +
                "where show_info_id in (select so.show_info_id from show_info si, show_open so, booking b, seat s " +
                "where si.show_info_id = so.show_info_id and s.show_id = so.show_id and b.seat_id = s.seat_id " +
                "and b.pay_status like \"Booked\" and genre = ? and booked_date between 20240601 and 20240608 group by so.show_info_id order by count(booking_id) desc);";
        return jdbcTemplate.query(sql, popularShowReviewRowMapper(), genre, startDate, endDate);
    }
}
