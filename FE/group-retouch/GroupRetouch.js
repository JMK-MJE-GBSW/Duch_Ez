document.addEventListener('DOMContentLoaded', function () {
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

    window.openMenu = openMenu;
    window.closeMenu = closeMenu;

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
});
