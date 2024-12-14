document.addEventListener("DOMContentLoaded", function () {
  const loginButton = document.querySelector(".login-btn");
  const usernameInput = document.getElementById("login-username");
  const passwordInput = document.getElementById("login-password");

  // 로그인 함수
  const login = async (username, password) => {
      const loginData = { username, password };

      try {
          const response = await fetch("http://localhost:8080/user/login", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json",
              },
              body: JSON.stringify(loginData),
          });

          if (!response.ok) {
              const errorMessage = await response.text();
              throw new Error(errorMessage);
          }     

          const responseData = await response.json(); // JSON 파싱
          const token = responseData.token; // "token" 속성 추출

          if (!token) {
              throw new Error("토큰이 반환되지 않았습니다.");
          }

          alert("로그인 성공" , token)

          // 로컬스토리지에 토큰 저장
          localStorage.setItem("authToken", `Bearer ${token}`);


          // 로그인 성공 시 리다이렉트
          window.location.href = "../home/LoginMain.html";
      } catch (error) {
          console.error("로그인 실패:", error.message);
          alert("로그인 실패: " + error.message);
      }
  };

  // 로그인 버튼 클릭 이벤트
  loginButton.addEventListener("click", function () {
      const username = usernameInput.value;
      const password = passwordInput.value;

      if (username && password) {
          login(username, password);
      } else {
          alert("아이디와 비밀번호를 입력해주세요.");
      }
  });
});
