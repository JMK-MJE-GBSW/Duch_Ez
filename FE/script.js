document.addEventListener('DOMContentLoaded', function() {
    const usernameInput = document.getElementById('username');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const passwordCheckInput = document.getElementById('password-check');
    const emailCheckInput = document.getElementById('email-check');
    const signupButton = document.querySelector('.signup-btn');

    const registerUser = async () => {
        const registerDto = {
            username: document.getElementById('username').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            password_check: document.getElementById('password-check').value,
            code: document.getElementById('code').value,
        };

        if (
            !registerDto.username ||
            !registerDto.email ||
            !registerDto.password ||
            !registerDto.password_check ||
            !registerDto.code
        ) {
            alert('모든 필드를 입력해주세요.');
            return;
        }

        if (registerDto.password !== registerDto.password_check) {
            alert('패스워드가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registerDto),
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage);
            }

            const successMessage = await response.text();
            alert(successMessage);
        } catch (error) {

            alert(`회원가입 실패: ${error.message}`);
        }
    };

    registerButton.addEventListener('click', registerUser);
});

//     const registerUser = async = () => {
//         username: document.getElementById('username').value,
//         email: document.getElementById('email').value,
//         password: document.getElementById('password').value,
//         password_check: document.getElementById('password-check').value,
//         code: document.getElementById('code').value,
//     };
//     if (
//         !registerDto.username || !registerDto.email || !registerDto.password || !registerDto.password_check || !registerDto.code) {
//             alert('모든 필드를 입력해주세요');
//             return;
//         }
    
//     if (registerDto.password !== registerDto.password_check) {
//         alert('패스워드가 일치하지 않습니다.');
//         return;
//     }
//     try {
//         const response = await fetch('/register' , {
//             method: 'POST',
//             headers: {
//                 'Content-Type' : 'application/json',
//             },
//             body: JSON.stringify(registerDto),
//         });

//     if (!response.ok) {
//         const errorMessage = await response.text();
//         throw new Error(errorMessage);
//     }

//     const successMessage = await response.text();
//     alert(successMessage);
//     } catch (error) {
//         alert(`회원가입 실패: ${error.message}`);
//     }
// };

// registerButton.addEventListener('click', registerUser);
// });