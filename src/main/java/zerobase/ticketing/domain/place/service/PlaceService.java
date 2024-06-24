package zerobase.ticketing.domain.place.service;

import org.springframework.stereotype.Service;
import zerobase.ticketing.domain.place.entity.Place;
import zerobase.ticketing.domain.place.repostitory.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public int registerPlace(List<Place> places) {

        // Insert 할 데이터 리스트 객체 생성
        List<Place> list = new ArrayList<>();

        // 매개변수 리스트의 좌석 파싱
        for (int i = 0; i < places.size(); i++) {
            Place curPlace = places.get(i);

            // 좌석번호 파싱
            StringTokenizer st = new StringTokenizer(curPlace.getSeatNo(), "~");
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            for (int j = start; j <= end; j++) {
                // 중복 좌석 체크
                if (placeRepository.existsPlace(curPlace.getVenue(), curPlace.getFloor(),
                        curPlace.getSector(), curPlace.getLine(), Integer.toString(j)) > 0) {
                    System.out.println("이미 등록되어 있는 좌석입니다.");
                    continue;
                }

                //파싱한 좌석 객체를 리스트에 추가
                Place newPlace = new Place();
                newPlace.setVenue(curPlace.getVenue());
                newPlace.setFloor(curPlace.getFloor());
                newPlace.setLine(curPlace.getLine());
                newPlace.setSector(curPlace.getSector());
                newPlace.setSeatNo(Integer.toString(j));
                list.add(newPlace);
            }
        }

        // Repository 로 전달
        try {
            placeRepository.batchInsert(list);
            return list.size(); // 리스트 중 중복된 좌석 빼고 추가된 좌석 수 반환
        } catch (Exception e) {
            throw new RuntimeException("좌석 등록에 실패하였습니다.");
        }
    }

    public List<Place> readPlace(String venue) {
        return placeRepository.findAllByName(venue);
    }

    public void updatePlaceVenue(String preVenue, String newVenue) {
        if (!placeRepository.existsByName(preVenue)) {
            throw new RuntimeException("존재하지 않는 공연장입니다.");
        }

        placeRepository.updatePlaceVenue(preVenue, newVenue);

    }

    public boolean existsPlace(Place place) {
        return placeRepository.existsPlace(place.getVenue(), place.getFloor(),
                place.getSector(), place.getLine(), place.getSeatNo()) > 0;
    }

    public void updatePlaceSeat(Place place) {

        // id를 뺀 좌석 정보를 비교하여, 변경될 좌석이 이미 있는지 확인
        if (existsPlace(place)) {
            throw new RuntimeException("공연장에 변경될 좌석이 존재합니다.");
        }

        placeRepository.updatePlaceSeat(place);
    }

    public void addPlaceSeat(Place place) {

        if (existsPlace(place)) {
            throw new RuntimeException("공연장에 이미 해당 좌석이 존재합니다.");
        }

        placeRepository.addPlaceSeat(place);
    }

    public void deletePlaceSeat(Place place) {
        placeRepository.deletePlaceSeat(place);
    }
}
