package zerobase.ticketing.domain.show.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.show.entity.ShowInfo;
import zerobase.ticketing.domain.show.entity.ShowOpen;
import zerobase.ticketing.domain.show.dto.SoonOpenShow;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ShowOpenRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShowOpenRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void batchInsert(List<ShowOpen> ShowOpenList) {
        String sql = "INSERT INTO show_open (show_info_id, show_date, show_time" +
                ", open_booking, open_booking_time, close_booking, close_booking_time, cast)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, ShowOpenList.get(i).getShowInfoId());
                        ps.setDate(2, Date.valueOf(ShowOpenList.get(i).getShowDate()));
                        ps.setTime(3, Time.valueOf(ShowOpenList.get(i).getShowTime()));
                        ps.setDate(4, Date.valueOf(ShowOpenList.get(i).getOpenBooking()));
                        ps.setTime(5, Time.valueOf(ShowOpenList.get(i).getOpenBookingTime()));
                        ps.setDate(6, Date.valueOf(ShowOpenList.get(i).getCloseBooking()));
                        ps.setString(7, String.valueOf(ShowOpenList.get(i).getCloseBookingTime()));
                        ps.setString(8, ShowOpenList.get(i).getCast());

                    }

                    @Override
                    public int getBatchSize() {
                        return ShowOpenList.size();
                    }
                });
    }

    public RowMapper<ShowOpen> showOpenRowMapper() {
        return (rs, rowNum) -> new ShowOpen(
                rs.getInt("show_id"),
                rs.getInt("show_info_id"),
                rs.getDate("show_date").toLocalDate(),
                rs.getTime("show_time").toLocalTime(),
                rs.getDate("open_booking").toLocalDate(),
                rs.getTime("open_booking_time").toLocalTime(),
                rs.getDate("close_booking").toLocalDate(),
                rs.getTime("close_booking_time").toLocalTime(),
                rs.getString("cast")
        );
    }

    public int existsShowOpen(int showInfoId, LocalDate showDate, LocalTime showTime) {

        String sql = "SELECT * FROM SHOW_OPEN WHERE show_info_id = ? AND show_date = ? AND show_time = ?";
        List<ShowOpen> list = jdbcTemplate.query(sql, showOpenRowMapper(), showInfoId, showDate, showTime);

        return list.size();
    }

    public int findShowId(int showInfoId, LocalDate showDate, LocalTime showTime) {

        String sql = "SELECT * FROM SHOW_OPEN WHERE show_info_id = ? AND show_date = ? AND show_time = ?";
        List<ShowOpen> list = jdbcTemplate.query(sql, showOpenRowMapper(), showInfoId, showDate, showTime);

        return list.get(0).getShowId();
    }

    public List<ShowOpen> findShowOpenListByShowInfoId(int showInfoId) {
        String sql = "SELECT * FROM SHOW_OPEN WHERE show_info_id = ?";
        List<ShowOpen> list = jdbcTemplate.query(sql, showOpenRowMapper(), showInfoId);

        return list;
    }

    public ShowOpen findShowByShowId(int showId) {
        String sql = "SELECT * FROM SHOW_OPEN WHERE show_id = ?";
        List<ShowOpen> list = jdbcTemplate.query(sql, showOpenRowMapper(), showId);

        return list.get(0);
    }

    public LocalDate findShowDateByShowId(int showId) {
        String sql = "SELECT * FROM SHOW_OPEN WHERE show_id = ?";
        List<ShowOpen> list = jdbcTemplate.query(sql, showOpenRowMapper(), showId);

        return list.get(0).getShowDate();
    }

    public void updateShowOpen(ShowOpen showOpen) {
        String sql = "UPDATE SHOW_OPEN SET show_info_id = ?, show_date = ?, show_time = ?, open_booking = ?" +
                ", open_booking_time = ?, close_booking = ?, close_booking_time = ?, cast = ?" +
                "WHERE show_id = ?";
        jdbcTemplate.update(sql, showOpen.getShowInfoId(), showOpen.getShowDate(), showOpen.getShowTime(), showOpen.getOpenBooking(),
                showOpen.getOpenBookingTime(), showOpen.getCloseBooking(), showOpen.getCloseBookingTime(), showOpen.getCast(),
                showOpen.getShowId());
    }

    public void deleteShowOpen(ShowOpen showOpen) {
        String sql = "DELETE FROM show_open WHERE show_id = ?";
        jdbcTemplate.update(sql, showOpen.getShowId());
    }

    public RowMapper<ShowInfo> popularShowRowMapper() {
        return (rs, rowNum) -> new ShowInfo(
                rs.getInt("show_info_id"),
                rs.getString("show_name"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getString("venue"),
                rs.getInt("run_time"),
                rs.getInt("age_group"),
                rs.getString("genre"),
                rs.getString("keyword"),
                rs.getString("cast"),
                rs.getInt("booked_seat"),
                rs.getInt("all_seat"),
                rs.getString("manager_id")
        );
    }

    public RowMapper<SoonOpenShow> soonOpenShowRowMapper() {
        return (rs, rowNum) -> new SoonOpenShow(
                rs.getInt("show_info_id"),
                rs.getDate("open_booking").toLocalDate(),
                rs.getTime("open_booking_time").toLocalTime(),
                rs.getString("show_name")
        );
    }

    public List<ShowInfo> readPopularShow(String genre, LocalDate startDate, LocalDate endDate) {
        String sql = "select si.* from show_info si, show_open so, booking b, seat s " +
                "where si.show_info_id = so.show_info_id and s.show_id = so.show_id and b.seat_id = s.seat_id " +
                "and b.pay_status like \"Booked\" and genre = ? and booked_date between ? and ? group by so.show_info_id order by count(booking_id) desc";
        return jdbcTemplate.query(sql, popularShowRowMapper(), genre, startDate, endDate);
    }

    public List<SoonOpenShow> readSoonOpenShow(LocalDate startDate, LocalDate endDate) {
        String sql = "select distinct so.show_info_id, open_booking, open_booking_time, si.show_name " +
                "from show_open so, show_info si " +
                "where so.show_info_id = si.show_info_id and open_booking between ? and ? order by open_booking;";
        return jdbcTemplate.query(sql, soonOpenShowRowMapper(), startDate, endDate);
    }
}
