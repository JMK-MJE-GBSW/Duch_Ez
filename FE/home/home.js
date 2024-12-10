document.addEventListener('DOMContentLoaded', function () {

    // 이벤트 리스너
    sendCodeButton.addEventListener('click', function () {
        console.log('인증번호 전송중입니다. 잠시만 기다려주세요.');
        alert('인증번호 전송중입니다. 잠시만 기다려주세요.');
        sendVerificationCode(); // 인증번호 발송 함수 호출
    });

    signupButton.addEventListener('click', registerUser);
}); 

// 사이드 메뉴 열기
function openMenu(menuId, overlayId) {
    const sideMenu = document.getElementById(menuId);
    const overlay = document.getElementById(overlayId);

    // 메뉴와 오버레이 활성화
    sideMenu.classList.add('active');
    overlay.classList.add('active');
}

function closeMenu(menuId, overlayId) {
    const sideMenu = document.getElementById(menuId);
    const overlay = document.getElementById(overlayId);

    // 메뉴와 오버레이 비활성화
    sideMenu.classList.remove('active');
    overlay.classList.remove('active');
}
