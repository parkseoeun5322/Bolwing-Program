<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	header { background-color: #00083d; }
	header img { width: 80px; margin: 5px 0 0 40px; }
</style>
</head>
<body>
	<header>
		<a href='<c:url value="/"/>'><img alt="메인" src="img/main_logo.png" width="50px;"></a>
		<button type="button" class="btn btn-danger" style="margin: 0 0 0 20px;"
				data-toggle="modal" data-target="#modal-danger">
			<i class="fas fa-map-marker-alt" style="margin: 0 5px 0 0;"></i><span style="font-size: 14px;">볼링장 위치 찾기</span>
		</button>
		<div class="modal modal-danger fade" id="modal-danger" style="display: none;">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title"><i class="fas fa-map-marker-alt" style="margin: 0 5px 0 0;"></i>볼링장 위치 찾기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">×</span></button>
              </div>
              <div class="modal-body">
              	<div style="margin: 0 0 20px 0;">
	              	<input type="text" id="modal-search-text" value="광주 볼링장"
	              		   onkeypress="if( event.keyCode==13 ){ searchBowling(); }"
	              		   style="line-height: 34px; width: 400px; padding-left: 15px;">
	              	<input type="button" id="modal-search" class="btn btn-outline btn-secondary"  
	              		   onclick="searchBowling();" value="검색">
              	</div>
                <div id="map" style="height: 300px;"></div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-outline pull-left btn-secondary" data-dismiss="modal">Close</button>
              </div>
            </div>
            <!-- /.modal-content -->
          </div>
          <!-- /.modal-dialog -->
        </div>
	</header>
	<script type="text/javascript">
		var container = document.getElementById("map");
		var modal = document.getElementById("modal-danger");
		var map = null;

		// 볼링장 위치찾기 버튼 클릭시 실행되는 이벤트 핸들러
		$(".btn-danger").on("click", function() {
			// 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
			var infowindow = new kakao.maps.InfoWindow({zIndex:1});
			
			// 보이지 않게 하다가 버튼 클릭 시 보이게 하는 모달 뷰같은 경우에는
			// 이 과정에서 먼저 map을 로드 시킨다면 map의 랜더링이 제대로 안되는 경우가 생기므로
			// 모달 뷰가 완전히 보인 다음에 맵을 생성해야하기 때문에 맵 생성 전 시간을 지연시킴
			setTimeout(function() {
				var options = {
					center: new kakao.maps.LatLng(35.15300, 126.91950),
					level: 3
				};
				
				map = new kakao.maps.Map(container, options);
				
				// 마커가 표시될 위치입니다  
				var markerPosition  = new kakao.maps.LatLng(35.15282509719269, 126.91947956816851); 

				// 마커를 생성합니다
				var marker = new kakao.maps.Marker({
				    position: markerPosition
				});

				// 마커가 지도 위에 표시되도록 설정합니다
				marker.setMap(map);
				
			}, 300);
		});

		// 모달 창의 검색 버튼의 onclick 속성에 걸려있는 이벤트 메소드
		function searchBowling() {
			// 마커를 클릭하면 장소명을 표출할 인포윈도우 입니다
			var infowindow = new kakao.maps.InfoWindow({zIndex:1});
			
			var ps = new kakao.maps.services.Places();
			ps.keywordSearch($("#modal-search-text").val(), placesSearchCB);

			// 키워드 검색 완료 시 호출되는 콜백함수 입니다
			function placesSearchCB (data, status, pagination) {
			    if (status === kakao.maps.services.Status.OK) {

			        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
			        // LatLngBounds 객체에 좌표를 추가합니다
			        var bounds = new kakao.maps.LatLngBounds();

			        for (var i = 0; i < data.length; i++) {
			            displayMarker(data[i]);    
			            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
			        }       

			        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
			        map.setBounds(bounds);
			    } 
			}

			// 지도에 마커를 표시하는 함수입니다
			function displayMarker(place) {
			    
			    // 마커를 생성하고 지도에 표시합니다
			    var marker = new kakao.maps.Marker({
			        map: map,
			        position: new kakao.maps.LatLng(place.y, place.x) 
			    });

			    // 마커에 클릭이벤트를 등록합니다
			    kakao.maps.event.addListener(marker, 'click', function() {
			        // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
			        infowindow.setContent('<div style="padding:5px;font-size:12px;">' 
					        				+ place.place_name + "<br>"
					        				+ place.road_address_name + '</div>');
			        infowindow.open(map, marker);
			    });
			}
		} //searchBowling()
	</script>
</body>
</html>