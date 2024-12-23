document.addEventListener('DOMContentLoaded', function () {
    const usernameInput = document.getElementById('username');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const passwordCheckInput = document.getElementById('password-check');
    const emailCheckInput = document.getElementById('email-check');
    const signupButton = document.getElementById('register-button');
    const sendCodeButton = document.getElementById('send-code');


    // 회원가입 로직
    const registerUser = async () => {
        const data = {
            username: usernameInput.value,
            email: emailInput.value,
            password: passwordInput.value,
            password_check: passwordCheckInput.value,
            code: emailCheckInput.value,
        };

        // 입력값 검증
        if (!data.username) {
            alert('사용자 이름을 입력해주세요.');
            return;
        }
        if (!data.email) {
            alert('이메일을 입력해주세요.');
            return;
        }
        if (!data.password) {
            alert('비밀번호를 입력해주세요.');
            return;
        }
        if (!data.password_check) {
            alert('비밀번호 확인을 입력해주세요.');
            return;
        }
        if (data.password !== data.password_check) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }
        if (!data.code) {
            alert('이메일 인증번호를 입력해주세요.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/user/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            alert('회원가입이 성공적으로 완료되었습니다.');
            window.location.href = 'login.html';
        } catch (error) {
            alert(`회원가입 실패: ${error.message}`);
        }
    };

    // 인증번호 발송 로직
    const sendVerificationCode = async () => {
        const data = {
            username: usernameInput.value,
            email: emailInput.value,
            password: passwordInput.value,
            password_check: passwordCheckInput.value,
        };

        // 입력 필드 검증
        if (!data.username || !data.email || !data.password || !data.password_check) {
            alert('인증코드 제외 모든 필드를 입력해주세요.');
            return;
        }

        if (data.password !== data.password_check) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/user/send-code', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            alert('인증번호가 이메일로 발송되었습니다.');
        } catch (error) {
            alert(`인증번호 발송 실패: ${error.message}`);
        }
    };

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
