package zerobase.ticketing.repository;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.Seat;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeatRepository {

    private final JdbcTemplate jdbcTemplate;

    public SeatRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(Seat seat) {
        String sql = "UPDATE SEAT SET booking_status = ? WHERE seat_id = ?";

        jdbcTemplate.update(sql, seat.getBookingStatus(), seat.getSeatId());
    }

    public void batchInsert(List<Seat> seatList) {
        String sql = "INSERT INTO SEAT (show_id, floor, sector, line, seat_no, price, booking_status)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, seatList.get(i).getShowId());
                        ps.setString(2, seatList.get(i).getFloor());
                        ps.setString(3, seatList.get(i).getSector());
                        ps.setString(4, seatList.get(i).getLine());
                        ps.setString(5, seatList.get(i).getSeatNo());
                        ps.setInt(6, seatList.get(i).getPrice());
                        ps.setString(7, seatList.get(i).getBookingStatus());
                    }

                    @Override
                    public int getBatchSize() {
                        return seatList.size();
                    }
                });
    }

    public RowMapper<Seat> seatRowMapper() {
        return (rs, rowNum) -> new Seat(
                rs.getInt("seat_id"),
                rs.getInt("show_id"),
                rs.getString("floor"),
                rs.getString("sector"),
                rs.getString("line"),
                rs.getString("seat_no"),
                rs.getInt("price"),
                rs.getString("booking_status")
        );
    }

    public int existsBySeat(int showId, String floor, String sector, String line, String seatNo, int price) {

        String sql = "";
        List<Seat> list = new ArrayList<>();

        if (sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM SEAT WHERE show_id = ? AND floor like ? " +
                    "AND line like ? AND seat_no like ? AND price = ?";
            list = jdbcTemplate.query(sql, seatRowMapper(), showId, floor, line, seatNo, price);
        } else if (!sector.isEmpty() && line.isEmpty()) {
            sql = "SELECT * FROM SEAT WHERE show_id = ? AND floor like ? " +
                    "AND sector like ? AND seat_no like ? AND price = ?";
            list = jdbcTemplate.query(sql, seatRowMapper(), showId, floor, sector, seatNo, price);
        } else if (!sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM SEAT WHERE show_id = ? AND floor like ?" +
                    "AND sector like ? AND line like ? AND seat_no like ? AND price = ?";
            list = jdbcTemplate.query(sql, seatRowMapper(), showId, floor, sector, line, seatNo, price);
        }

        return list.size();
    }

    public String findBookedStatusBySeatId(int seatId) {
        String sql = "SELECT * FROM SEAT WHERE seat_id = ?";
        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), seatId);

        return list.get(0).getBookingStatus();
    }

    public List<Seat> findSeatListByShowId(int showId) {
        String sql = "SELECT * FROM SEAT WHERE show_id = ?";
        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), showId);

        return list;
    }

    public int findSeatIdBySeat(Seat seat) {
        String sql = "SELECT * FROM SEAT WHERE show_id = ? AND floor = ? AND sector = ? " +
                "AND line = ? AND seat_no = ?";
        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), seat.getShowId(), seat.getFloor(),
                seat.getSector(), seat.getLine(), seat.getSeatNo());

        return list.get(0).getSeatId();
    }

    public Seat findSeatBySeatId(int seatId) {
        String sql = "SELECT * FROM SEAT WHERE seat_id = ?";
        List<Seat> list = jdbcTemplate.query(sql, seatRowMapper(), seatId);

        return list.get(0);
    }

    public void updateSeat(Seat seat) {
        String sql = "UPDATE SEAT SET floor = ?, sector = ?, line = ?, seat_no = ?" +
                ", price = ?, booking_status = ?" +
                "WHERE seat_id = ?";
        jdbcTemplate.update(sql, seat.getFloor(), seat.getSector(), seat.getLine(),
                seat.getSeatNo(), seat.getPrice(), seat.getBookingStatus(), seat.getSeatId());
    }

    public void deleteSeat(int seatId) {
        String sql = "DELETE FROM SEAT WHERE seat_id = ?";
        jdbcTemplate.update(sql, seatId);
    }

    public void addSeat(Seat seat) {
        String sql = "INSERT INTO SEAT (show_id, floor, sector, line, seat_no, price, booking_status)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, seat.getShowId(), seat.getShowId(), seat.getFloor(), seat.getSector(),
                seat.getLine(), seat.getSeatNo(), seat.getPrice(), seat.getBookingStatus());
    }
}
