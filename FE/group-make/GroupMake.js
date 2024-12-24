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


const apiUrl = "http://localhost:8080/group"; // API 경로 (필요시 수정)

// 사이드 메뉴 열기 + 그룹 목록 가져오기
async function openMenu(menuId, overlayId) {
    const sideMenu = document.getElementById(menuId);
    const overlay = document.getElementById(overlayId);

    let groups = []; // 기본 빈 그룹 목록

    try {
        // 그룹 데이터 가져오기
        groups = await fetchGroups();
    } catch (error) {
        console.error("Failed to load group data:", error);

        // 기본 메시지 렌더링
        renderGroupNames([]);
    }

    // 그룹 이름 렌더링 (빈 목록이어도 진행)
    renderGroupNames(groups);

    // 메뉴와 오버레이 활성화
    sideMenu.classList.add('active');
    overlay.classList.add('active');
}

// 그룹 이름 렌더링
function renderGroupNames(groups) {
    const groupListContainer = document.getElementById("recent-group-list");
    groupListContainer.innerHTML = ""; // 기존 목록 초기화

    if (groups.length === 0) {
        // 그룹이 없을 때 기본 메시지 표시
        const emptyMessage = document.createElement("div");
        emptyMessage.className = "group-item empty";
        emptyMessage.textContent = "등록된 그룹이 없습니다.";
        groupListContainer.appendChild(emptyMessage);
    } else {
        // 그룹이 있을 때
        groups.forEach(group => {
            const groupItem = document.createElement("div");
            groupItem.className = "group-item";
            groupItem.textContent = group.name;

            // 그룹 클릭 시 리다이렉트 (그룹 이름을 URL 파라미터로 전달)
            groupItem.addEventListener("click", function () {
                window.location.href = `../Group-complete/Group-before.html?name=${encodeURIComponent(group.name)}`;
            });

            groupListContainer.appendChild(groupItem);
        });
    }
}

// 백엔드에서 그룹 데이터 가져오기
async function fetchGroups() {
    const token = localStorage.getItem("authToken"); // Bearer 포함된 토큰 가져오기

    if (!token) {
        throw new Error("로그인 토큰이 없습니다. 로그인을 먼저 해주세요.");
    }

    const response = await fetch(apiUrl, {
        method: "GET",
        headers: {
            "Authorization": token, // Authorization 헤더에 Bearer 추가
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error("Failed to fetch groups. Status: " + response.status);
    }

    return response.json(); // JSON 데이터를 반환
}

// 사이드 메뉴 닫기
function closeMenu(menuId, overlayId) {
    const sideMenu = document.getElementById(menuId);
    const overlay = document.getElementById(overlayId);

    // 메뉴와 오버레이 비활성화
    sideMenu.classList.remove('active');
    overlay.classList.remove('active');
}

// 로그아웃 함수
function logout() {
    localStorage.clear();

    localStorage.removeItem('currentGroupName');  // 그룹 이름 삭제
    // 추가 작업 (예: 사용자 알림)
    alert("로그아웃 되었습니다.");

    // 로그인 페이지로 리디렉션
    location.href = '../user/login.html';
}

// 로그인 처리 함수
function login(username, password) {
    localStorage.clear();  // 로그인 시 이전 데이터 삭제

    // 로그인 처리 로직 추가 (예: 로그인 API 호출 후 토큰 저장)
    const token = "some_token";  // 실제 로그인 API에서 받아온 토큰을 여기에 저장
    localStorage.setItem("authToken", token);

    // 로그인 후 리디렉션
    location.href = '../dashboard/dashboard.html';  // 로그인 후 대시보드나 원하는 페이지로 이동
}
