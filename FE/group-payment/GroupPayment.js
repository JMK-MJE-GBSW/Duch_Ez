document.addEventListener('DOMContentLoaded', function () {
    // 오늘 날짜를 자동으로 설정하는 함수
    function setTodayDate(inputId) {
        const dateInput = document.getElementById(inputId);
        if (dateInput) {
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0'); // 월 (2자리)
            const dd = String(today.getDate()).padStart(2, '0'); // 일 (2자리)
            dateInput.value = `${yyyy}-${mm}-${dd}`;
        } else {
            console.error(`ID가 ${inputId}인 입력 요소를 찾을 수 없습니다.`);
        }
    }

    // 메뉴 열기 함수
    function openMenu(menuId, overlayId) {
        const sideMenu = document.getElementById(menuId);
        const overlay = document.getElementById(overlayId);

        if (sideMenu && overlay) {
            sideMenu.classList.add('active');
            overlay.classList.add('active');
        } else {
            console.error('메뉴 또는 오버레이 요소를 찾을 수 없습니다.');
        }
    }

    // 메뉴 닫기 함수
    function closeMenu(menuId, overlayId) {
        const sideMenu = document.getElementById(menuId);
        const overlay = document.getElementById(overlayId);

        if (sideMenu && overlay) {
            sideMenu.classList.remove('active');
            overlay.classList.remove('active');
        } else {
            console.error('메뉴 또는 오버레이 요소를 찾을 수 없습니다.');
        }
    }

    // 전역 함수로 노출
    window.openMenu = openMenu;
    window.closeMenu = closeMenu;

    // 햄버거 메뉴 아이콘 및 오버레이 설정
    const hamburgerIcon = document.getElementById('GroupCompleteFa-bar');
    const overlay = document.getElementById('GroupCompleteOverlay');

    if (hamburgerIcon && overlay) {
        hamburgerIcon.addEventListener('click', function () {
            openMenu('GroupComplete-SideMenu', 'GroupCompleteOverlay');
        });

        overlay.addEventListener('click', function () {
            closeMenu('GroupComplete-SideMenu', 'GroupCompleteOverlay');
        });
    }

    // 날짜 입력창과 아이콘 동작 설정
    const dateBox = document.querySelector('.GroupPayment-DateBox');
    const dateInput = document.getElementById('GroupPayment-BoxDate');
    const calendarIcon = document.getElementById('calendar-icon');

    if (dateBox && dateInput && calendarIcon) {
        function showDatePicker() {
            dateInput.focus();
            dateInput.click();
            dateInput.showPicker();
        }

        dateBox.addEventListener('click', showDatePicker);
        calendarIcon.addEventListener('click', showDatePicker);
    }

    // 날짜 입력창에 오늘 날짜 자동 설정
    setTodayDate('GroupPayment-BoxDate');
});
