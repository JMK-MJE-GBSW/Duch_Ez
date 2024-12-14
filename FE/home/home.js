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

// 그룹 이름만 렌더링
function renderGroupNames(groups) {
    const groupListContainer = document.getElementById("recent-group-list");
    groupListContainer.innerHTML = ""; // 기존 목록 초기화

    if (groups.length === 0) {
        // 그룹이 없을 때 기본 메시지 표시
        const emptyMessage = document.createElement("div");
        emptyMessage.className = "group-item empty"; // 스타일링 클래스
        emptyMessage.textContent = "등록된 그룹이 없습니다.";
        groupListContainer.appendChild(emptyMessage);
    } else {
        // 그룹이 있을 때
        for (let i = 0; i < groups.length; i++) {
            const group = groups[i];
            const groupItem = document.createElement("div");
            groupItem.className = "group-item"; // 스타일링 클래스
            groupItem.textContent = group.name; // 그룹 이름만 표시

            // 그룹 클릭 시 리다이렉트
            groupItem.addEventListener("click", function () {
                // 예: 그룹 상세 페이지로 이동 (그룹 ID를 URL에 포함)
                window.location.href = `../group-detail/groupDetail.html?id=${group.id}`;
            });

            groupListContainer.appendChild(groupItem);
        }
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
            "Authorization": `Bearer ${token}`, // Authorization 헤더에 Bearer 추가
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
