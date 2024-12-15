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


// URL에서 그룹 이름 추출
const params = new URLSearchParams(window.location.search);
const groupName = params.get("name");

if (!groupName) {
    alert("그룹 이름이 제공되지 않았습니다.");
    throw new Error("그룹 이름이 없습니다.");
}


async function fetchGroupDetail(groupName) {
    const token = localStorage.getItem("authToken"); // Bearer 포함된 토큰 가져오기

    const response = await fetch(`http://localhost:8080/group/${groupName}`, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error("Failed to fetch group details. Status: " + response.status);
    }

    return response.json(); // 그룹 상세 정보 반환
}


function renderGroupDetail(groupDetail) {
    const groupContainer = document.querySelector(".Group");

    // 그룹 이름
    const groupNameElement = document.createElement("h1");
    groupNameElement.textContent = groupDetail.name;

    // 참가자 목록
    const participantList = document.createElement("ul");
    groupDetail.participants.forEach(participant => {
        const listItem = document.createElement("li");
        listItem.textContent = participant.name; // 참가자 이름
        participantList.appendChild(listItem);
    });

    // 그룹 정보를 페이지에 추가
    groupContainer.innerHTML = ""; // 기존 내용 초기화
    groupContainer.appendChild(groupNameElement);
    groupContainer.appendChild(participantList);
}

// 데이터 가져오기 및 렌더링 실행
fetchGroupDetail(groupName)
    .then(groupDetail => renderGroupDetail(groupDetail))
    .catch(error => console.error("Error loading group detail:", error));
