window.Kakao.init("c42d9650f0d0202aac6c47d98ea0c55d");

const RESTkey = "c42d9650f0d0202aac6c47d98ea0c55d";
const RedirectURL = "http://localhost:8080/webprog/main/Main.jsp";

var kakaoAuthAccessToken = null, KakaoUser = null;

function kakaoLogin() {
	if (!Kakao.Auth.getAccessToken()) {
		window.Kakao.Auth.login({
			success: function(authObj) {
				kakaoAuthAccessToken = authObj.access_token;
				window.Kakao.API.request({
					url: '/v2/user/me',
					success: res => {
						console.log(res);
						KakaoUser = res;
						sessionStorage.kakaoId = res.id;
						sessionStorage.kakaoName = res.properties.nickname;
						
						updateLoginText();
					}
				});
			}
		});
	}
	else { // 마이 페이지
		const form = document.createElement('form'); // form 태그 생성
		form.setAttribute('method', 'post'); // 전송 방식 결정 (get or post)
		form.setAttribute('action', 'mypage.jsp'); // 전송할 url 지정
		
	    const id = document.createElement('input');
		id.setAttribute('type', 'hidden');
		id.setAttribute('name', 'id')
		id.setAttribute('value', sessionStorage.getItem('kakaoId'));
		form.appendChild(id);
		
		document.body.appendChild(form);
		console.log(form);
		form.submit();
	}
}

function kakaoLogout() {
	window.Kakao.Auth.logout(function() { // 카카오 로그아웃
        kakaoAuthAccessToken = null; // 로그아웃 후에 초기화
        KakaoUser = null;
        sessionStorage.removeItem("kakaoId");
        sessionStorage.removeItem("kakaoName");
		updateLoginText();
		window.location.href='https://kauth.kakao.com/oauth/logout?client_id=' + RESTkey +  
		'&logout_redirect_uri=' + RedirectURL;
    });
}

// 텍스트 업데이트 함수
function updateLoginText() {
    var loginOutElement = $("#Login_out");
    console.log("Access Token:", Kakao.Auth.getAccessToken()); // 여기에 로그 추가

    var text = (Kakao.Auth.getAccessToken() !== null) ? "My Page" : "Log in";

    $(document).ready(function() {
        loginOutElement.attr("value", text);
    });
}
