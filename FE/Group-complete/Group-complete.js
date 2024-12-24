document.addEventListener('DOMContentLoaded', function () {
    const itemListContainer = document.querySelector('.dutch-pay-items');
    const groupContainer = document.querySelector('.Group');
    const debtListContainer = document.querySelector('.debt-list');
    let currentGroupName = null;

    async function fetchGroupDetail(groupName) {
        try {
            const token = localStorage.getItem("authToken");
            const response = await fetch(`http://localhost:8080/group/${groupName}`, {
                method: "GET",
                headers: {
                    "Authorization": token,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch group details. Status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            alert("그룹 정보를 가져오는 데 실패했습니다.");
            console.error(error);
        }
    }

    async function fetchDutchPayItems(groupName) {
        try {
            const token = localStorage.getItem("authToken");
            const response = await fetch(`http://localhost:8080/group/${groupName}/duch-pay`, {
                method: "GET",
                headers: {
                    "Authorization": token,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch dutch pay items. Status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            alert("더치페이 항목을 가져오는 데 실패했습니다.");
            console.error(error);
        }
    }

    async function fetchDebtRelations(groupName) {
        try {
            const token = localStorage.getItem("authToken");
            const response = await fetch(`http://localhost:8080/group/${groupName}/debts`, {
                method: "GET",
                headers: {
                    "Authorization": token,
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) throw new Error(`Failed to fetch debt relations. Status: ${response.status}`);
            return await response.json();z
        } catch (error) {
            alert("채무 관계를 가져오는 데 실패했습니다.");
            console.error(error);
        }
    }

    function renderGroupDetail(groupDetail) {
        groupContainer.innerHTML = ""; // 기존 내용 비우기
        const groupNameElement = document.createElement("h1");
        groupNameElement.classList.add("group-name"); // 그룹 이름에 스타일 추가
        groupNameElement.textContent = groupDetail.name;
    
        const participantList = document.createElement("div");
        participantList.classList.add("participant-list"); // 참가자 목록에 클래스 추가
        groupDetail.participants.forEach(participant => {
            const listItem = document.createElement("span");
            listItem.classList.add("participant-item"); // 각 참가자에 대한 스타일 추가
            listItem.textContent = participant.name;
            participantList.appendChild(listItem);
        });
    
        const totalSpentElement = document.createElement("p");
        totalSpentElement.classList.add("total-spent");
        totalSpentElement.textContent = `전체 금액: ${groupDetail.totalSpent.toLocaleString()}원`;
    
        groupContainer.appendChild(groupNameElement);
        groupContainer.appendChild(participantList);
        groupContainer.appendChild(totalSpentElement);
        currentGroupName = groupDetail.name;
        localStorage.setItem("currentGroupName", currentGroupName);
    }
    
    

    function renderItemList(items) {
        itemListContainer.innerHTML = "";
        if (!items || items.length === 0) {
            itemListContainer.innerHTML = "<p>항목이 없습니다.</p>";
            return;
        }

        items.forEach(item => {
            const itemElement = document.createElement("div");
            itemElement.classList.add("item");
            const titleElement = document.createElement("h3");
            titleElement.textContent = item.title || "제목 없음";
            const detailsElement = document.createElement("p");
            detailsElement.textContent = `총액: ${item.totalAmount}원`;
            itemElement.appendChild(titleElement);
            itemElement.appendChild(detailsElement);
            itemListContainer.appendChild(itemElement);
        });
    }

    function renderDebtRelations(debts) {
        debtListContainer.innerHTML = "";
        if (!debts || Object.keys(debts).length === 0) {
            debtListContainer.innerHTML = "<p>채무 관계가 없습니다.</p>";
            return;
        }

        Object.entries(debts).forEach(([creditor, debtors]) => {
            Object.entries(debtors).forEach(([debtor, amount]) => {
                const debtItem = document.createElement("div");
                debtItem.classList.add("debt-item");
                debtItem.textContent = `${creditor} → ${debtor}: ${amount.toLocaleString()}원`;
                debtListContainer.appendChild(debtItem);
            });
        });
    }

    const params = new URLSearchParams(window.location.search);
    let groupName = params.get("name");
    if (!groupName) {
        groupName = localStorage.getItem("currentGroupName");
    } else {
        localStorage.setItem("currentGroupName", groupName);
    }

    if (!groupName) {
        alert("그룹 이름이 제공되지 않았습니다.");
        return;
    }

    fetchGroupDetail(groupName).then(groupDetail => {
        if (groupDetail) renderGroupDetail(groupDetail);
    });

    fetchDutchPayItems(groupName).then(dutchPayItems => {
        if (dutchPayItems) renderItemList(dutchPayItems);
    });

    fetchDebtRelations(groupName).then(debts => {
        if (debts) renderDebtRelations(debts);
    });
});

function goToGroupPaymentPage() {
    const groupName = localStorage.getItem("currentGroupName");
    if (!groupName) {
        alert("그룹 이름이 제공되지 않았습니다.");
        return;
    }
    location.href = `../group-payment/GroupPayment.html?name=${encodeURIComponent(groupName)}`;
}

function openMenu(menuId, overlayId) {
    document.getElementById(menuId).classList.add('active');
    document.getElementById(overlayId).classList.add('active');
}

function closeMenu(menuId, overlayId) {
    document.getElementById(menuId).classList.remove('active');
    document.getElementById(overlayId).classList.remove('active');
}


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
