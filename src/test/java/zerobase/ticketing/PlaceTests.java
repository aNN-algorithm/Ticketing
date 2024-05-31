package zerobase.ticketing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.ticketing.domain.Place;
import zerobase.ticketing.repository.PlaceRepository;
import zerobase.ticketing.service.PlaceService;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
class PlaceTests {

	private final PlaceService placeService;
	private final PlaceRepository placeRepository;

	@Autowired
	public PlaceTests(PlaceService placeService, PlaceRepository placeRepository) {
		this.placeService = placeService;
		this.placeRepository = placeRepository;
	}

	@Test
	@Transactional
	public void registerPlace() {
		// given
		// 24 좌석 삽입(registerPlace)
		// id : auto increment
		List<Place> placeList = Arrays.asList(
				new Place(1, "샤롯데시어터", "1층", "A구역", "2열", "11~14"),
				new Place(2, "샤롯데시어터", "1층", "B구역", "2열", "15~30"),
				new Place(3, "샤롯데시어터", "1층", "C구역", "2열", "31~34")
		); // 24좌석

		placeService.registerPlace(placeList);

		// when
		List<Place> insertedList = placeRepository.findAllByName("샤롯데시어터");

		// then
		assertEquals(24, insertedList.size());
	}

	@Test
	@Transactional
	public void readPlace() {
	    // given
		// 24 좌석 삽입(registerPlace)
		// id : auto increment
		List<Place> placeList = Arrays.asList(
				new Place(1, "샤롯데시어터", "1층", "A구역", "2열", "11~14"),
				new Place(2, "샤롯데시어터", "1층", "B구역", "2열", "15~30"),
				new Place(3, "샤롯데시어터", "1층", "C구역", "2열", "31~34")
		); // 24좌석

		placeService.registerPlace(placeList);

	    // when
		List<Place> insertedList = placeService.readPlace("샤롯데시어터");
		List<Place> testList = placeRepository.findAllByName("샤롯데시어터");

	    // then
		assertEquals(insertedList.size	(), testList.size());
	}

	@Test
	@Transactional
	void updatePlaceSeat() {
	    // given
		// 1 좌석 삽입(registerPlace)
		// id : auto increment
		List<Place> placeList = Arrays.asList(
				new Place(700000, "샤롯데시어터", "1층", "A구역", "2열", "11~11")
		); // 1좌석

		placeService.registerPlace(placeList);

	    // when
		int preId = placeRepository.findId("샤롯데시어터", "1층", "A구역", "2열", "11");
		Place place = new Place(preId, "샤롯데시어터", "1층", "A구역", "2열", "111");
		placeService.updatePlaceSeat(place);

	    // then
		int changedId = placeRepository.findId("샤롯데시어터", "1층", "A구역", "2열", "111");
		assertEquals(preId, changedId);
	}

	@Test
	@Transactional
	void deletePlaceSeat() {
	    // given
		// 1 좌석 삽입(registerPlace)
		// id : auto increment
		List<Place> placeList = Arrays.asList(
				new Place(1, "샤롯데시어터", "1층", "A구역", "2열", "11~11")
		); // 1좌석

		placeService.registerPlace(placeList);
		int preId = placeRepository.findId("샤롯데시어터", "1층", "A구역", "2열", "11");

	    // when
		Place place = new Place(preId, "샤롯데시어터", "1층", "A구역", "2열", "11");
		placeService.deletePlaceSeat(place);

		int resultId = placeRepository.existsPlace("샤롯데시어터", "1층", "A구역", "2열", "11");

		assertEquals(0, resultId);
		// then
	}

}
