package zerobase.ticketing.domain.place.repostitory;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.ticketing.domain.place.entity.Place;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PlaceRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void batchInsert(List<Place> places) {
        String sql = "INSERT INTO PLACE (venue, floor, sector, line, seat_no) " +
                "VALUES (?, ?, ?, ?, ?) ";

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, places.get(i).getVenue());
                        ps.setString(2, places.get(i).getFloor());
                        ps.setString(3, places.get(i).getSector());
                        ps.setString(4, places.get(i).getLine());
                        ps.setString(5, places.get(i).getSeatNo());
                    }

                    @Override
                    public int getBatchSize() {
                        return places.size();
                    }
                });
    }

    public boolean existsByName(String venue) {
        // Venue 이름 중복 확인

        String sql = "SELECT * FROM PLACE WHERE venue like ?";
        List<Place> list = jdbcTemplate.query(sql, placeRowMapper(), venue);

        return !list.isEmpty();
    }

    public List<Place> findAllByName(String venue) {
        // 제목으로 공연장 찾기

        String sql = "SELECT * FROM PLACE WHERE venue like ?";
        List<Place> list = jdbcTemplate.query(sql, placeRowMapper(), venue);
        return list;
    }

    public RowMapper<Place> placeRowMapper() {
        return (rs, rowNum) -> new Place(
                rs.getInt("id"),
                rs.getString("venue"),
                rs.getString("floor"),
                rs.getString("sector"),
                rs.getString("line"),
                rs.getString("seat_no")
        );
    }

    public int existsPlace(String venue, String floor, String sector, String line, String seatNo) {

        String sql;
        List<Place> list = new ArrayList<>();

        if (sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue LIKE ? AND floor LIKE ? " +
                    "AND line LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, line, seatNo);
        } else if (!sector.isEmpty() && line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue LIKE ? AND floor LIKE ? " +
                    "AND sector LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, sector, seatNo);
        } else if (!sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue like ? AND floor LIKE ?" +
                    "AND sector LIKE ? AND line LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, sector, line, seatNo);
        }

        return list.size();
    }

    public int findId(String venue, String floor, String sector, String line, String seatNo) {

        String sql;
        List<Place> list = new ArrayList<>();

        if (sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue LIKE ? AND floor LIKE ? " +
                    "AND line LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, line, seatNo);
        } else if (!sector.isEmpty() && line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue LIKE ? AND floor LIKE ? " +
                    "AND sector LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, sector, seatNo);
        } else if (!sector.isEmpty() && !line.isEmpty()) {
            sql = "SELECT * FROM PLACE WHERE venue like ? AND floor LIKE ?" +
                    "AND sector LIKE ? AND line LIKE ? AND seat_no LIKE ?";
            list = jdbcTemplate.query(sql, placeRowMapper(), venue, floor, sector, line, seatNo);
        }

        return list.get(0).getId();
    }

    public void updatePlaceVenue(String preVenue, String newVenue) {
        String sql = "UPDATE PLACE SET venue = ? WHERE venue like ?";
        jdbcTemplate.update(sql, newVenue, preVenue);
    }

    public void addPlaceSeat(Place place) {
        String sql = "INSERT INTO PLACE (venue, floor, sector, line, seat_no) " +
                "VALUES (?, ?, ?, ?, ?) ";

        try {
            jdbcTemplate.update(sql, place.getVenue(), place.getFloor(), place.getSector(),
                    place.getLine(), place.getSeatNo());
        } catch (Exception e){
            throw new RuntimeException("DB에 등록을 실패하였습니다.");
        }
    }

    public void updatePlaceSeat(Place place) {
        String sql = "UPDATE PLACE SET floor = ?, sector = ?, line = ?, seat_no = ?" +
                "WHERE id = ?";
        jdbcTemplate.update(sql, place.getFloor(), place.getSector()
                , place.getLine(), place.getSeatNo(), place.getId());
    }

    public void deletePlaceSeat(Place place) {
        String sql = "DELETE FROM PLACE WHERE venue Like ? AND floor Like ? " +
                "AND sector Like ? AND line Like ? AND seat_no LIKE ?";

        try {
            jdbcTemplate.update(sql, place.getVenue(), place.getFloor(),
                    place.getSector(), place.getLine(), place.getSeatNo());
        } catch (Exception e) {
            throw new RuntimeException("DB 삭제에 실패하였습니다.");
        }
    }
}
