document.addEventListener('DOMContentLoaded', function () {
    // openMenu 함수 수정
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

    // closeMenu 함수 수정
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

    // 글로벌 스코프에 함수 노출
    window.openMenu = openMenu;
    window.closeMenu = closeMenu;

    // 햄버거 아이콘과 오버레이에 이벤트 리스너 추가
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

    // 참가자 추가 기능
    const addButton = document.getElementById('member-extra');
    const inputField = document.getElementById('GroupMake-BoxText2');
    const memberList = document.getElementById('member-list');

    if (!addButton || !inputField || !memberList) {
        console.error('필요한 요소 중 하나를 찾을 수 없습니다.');
        return;
    }

    addButton.addEventListener('click', function () {
        const inputValue = inputField.value.trim();

        if (inputValue === '') {
            alert('참가자 이름을 입력해주세요.');
            return;
        }

        const existingMembers = Array.from(memberList.children).map(
            (member) => member.textContent.replace(' 삭제', '')
        );
        if (existingMembers.includes(inputValue)) {
            alert('이미 추가된 참가자입니다.');
            return;
        }

        const newMember = document.createElement('div');
        newMember.className = 'member-item';
        newMember.textContent = inputValue;

        const deleteButton = document.createElement('button');
        deleteButton.style.backgroundColor = 'transparent';
        deleteButton.style.border = 'none';
        deleteButton.style.cursor = 'pointer';

        const deleteIcon = document.createElement('i');
        deleteIcon.className = 'fa-solid fa-xmark';
        deleteIcon.style.color = '#f0f0f0';
        deleteIcon.style.fontSize = '15px';
        deleteButton.appendChild(deleteIcon);

        deleteButton.addEventListener('click', function () {
            if (confirm('정말로 삭제하시겠습니까?')) {
                memberList.removeChild(newMember);
            }
        });

        newMember.appendChild(deleteButton);
        memberList.appendChild(newMember);

        inputField.value = '';
    });
});
