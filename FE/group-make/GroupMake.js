document.addEventListener('DOMContentLoaded', function () {
    // 메뉴 열기/닫기 함수
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

    // 함수 전역 등록
    window.openMenu = openMenu;
    window.closeMenu = closeMenu;

    // 참가자 추가 기능
    const addButton = document.getElementById('member-extra');
    const inputField = document.getElementById('GroupMake-BoxText2');
    const memberList = document.getElementById('member-list');
    const createButton = document.querySelector('.GroupMake-btn');
    const groupNameInput = document.getElementById('GroupMake-BoxText');

    if (!addButton || !inputField || !memberList || !groupNameInput) {
        console.error('필수 요소를 찾을 수 없습니다.');
        return;
    }

    // 참가자 추가 버튼 클릭 이벤트
    addButton.addEventListener('click', () => {
        const inputValue = inputField.value.trim();

        if (!inputValue) {
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
        deleteButton.className = 'delete-button';
        deleteButton.innerHTML = '<i class="fa-solid fa-xmark" style="color: #f0f0f0; font-size: 15px;"></i>';

        deleteButton.addEventListener('click', () => {
            if (confirm('정말로 삭제하시겠습니까?')) {
                memberList.removeChild(newMember);
            }
        });

        newMember.appendChild(deleteButton);
        memberList.appendChild(newMember);

        inputField.value = '';
    });

    // 그룹 생성 요청 함수
    const createGroup = async () => {
        const groupName = groupNameInput.value.trim();
        const members = Array.from(memberList.children).map((member) => 
            member.textContent.replace(' 삭제', '')
        );

        if (!groupName) {
            alert('그룹 이름을 입력해주세요.');
            return;
        }

        if (members.length < 2) {  // 참가자가 2명 이상이어야 함
            alert('참가자는 최소 2명 이상이어야 합니다.');
            return;
        }

        const token = localStorage.getItem("authToken");

        const data = {
            name: groupName,
            participants: members
        };

        try {
            const response = await fetch('http://localhost:8080/group/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token // 토큰 포함
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                throw new Error(errorMessage || '그룹 생성 실패');
            }

            // 서버가 성공 응답을 보냈다면
            alert('그룹이 성공적으로 생성되었습니다.');
            
            // 그룹 이름을 URL 파라미터로 전달하여 페이지 이동
            window.location.href = `GroupMakeComplete.html?groupName=${encodeURIComponent(groupName)}`;
        } catch (error) {
            alert(`그룹 생성 실패: ${error.message}`);
        }
    };

    // 그룹 만들기 버튼 이벤트
    createButton.addEventListener('click', createGroup);
});
