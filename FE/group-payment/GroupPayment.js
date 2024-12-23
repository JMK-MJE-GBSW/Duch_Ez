let groupName = null; // 전역 변수 선언

document.addEventListener('DOMContentLoaded', async function () {
    groupName = getGroupNameFromURL(); // groupName을 전역 변수에 할당

    if (!groupName) {
        alert("그룹 이름이 제공되지 않았습니다. URL을 확인해 주세요.");
        console.error("URL에 'name' 파라미터가 존재하지 않습니다.");
        location.href = "../home/SignupMain.html"; // 홈으로 리다이렉트
        return;
    }

    try {
        const groupData = await fetchGroupParticipants(groupName);
        renderParticipants(groupData.participants);
    } catch (error) {
        console.error("참가자 데이터를 가져오는 중 오류 발생:", error);
        alert("참가자 데이터를 가져오는 중 문제가 발생했습니다.");
    }

    setTodayDate("payment-date");
});

// URL에서 그룹 이름 가져오기
function getGroupNameFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("name");
}

// 오늘 날짜 설정
function setTodayDate(inputId) {
    const dateInput = document.getElementById(inputId);
    if (dateInput) {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        dateInput.value = `${yyyy}-${mm}-${dd}`;
    }
}

// 서버에서 참가자 목록 가져오기
async function fetchGroupParticipants(groupName) {
    const token = localStorage.getItem("authToken");
    const response = await fetch(`http://localhost:8080/group/${groupName}`, {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error(`Failed to fetch group participants. Status: ${response.status}`);
    }

    return response.json();
}

// 참가자 목록을 화면에 렌더링
function renderParticipants(participants) {
    const payerSelect = document.getElementById("payer-select");
    const checkboxesContainer = document.getElementById("participants-checkboxes");

    participants.forEach(participant => {
        // 결제자 옵션 추가
        const option = document.createElement("option");
        option.value = participant.id;
        option.textContent = participant.name;
        payerSelect.appendChild(option);

        // 참가자 체크박스 추가
        const checkboxLabel = document.createElement("label");
        checkboxLabel.classList.add("participant-checkbox");

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.value = participant.id;

        checkboxLabel.appendChild(checkbox);
        checkboxLabel.append(` ${participant.name}`);
        checkboxesContainer.appendChild(checkboxLabel);
    });
}

// 결제 항목 데이터 제출
function submitPayment() {
    const title = document.getElementById("item-title").value.trim();
    const totalAmount = document.getElementById("total-amount").value.trim();
    const payerId = document.getElementById("payer-select").value;
    const date = document.getElementById("payment-date").value;

    const selectedParticipants = [];
    document.querySelectorAll("#participants-checkboxes input:checked").forEach(checkbox => {
        selectedParticipants.push(checkbox.value);
    });

    if (!title || !totalAmount || !payerId || selectedParticipants.length === 0) {
        alert("모든 항목을 입력하고 적어도 한 명의 참여자를 선택해주세요.");
        return;
    }

    const paymentData = {
        title: title,
        totalAmount: parseInt(totalAmount, 10),
        paymentDate: date,
        payerId: payerId,
        participantIds: selectedParticipants
    };

    console.log("전송될 결제 항목 데이터:", paymentData);

    fetch(`http://localhost:8080/group/${groupName}/duch-pay`, { // 전역 groupName 사용
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": localStorage.getItem("authToken"),
        },
        body: JSON.stringify(paymentData),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("결제 항목 생성 실패");
        }
        return response.json();
    })
    .then(data => {
        alert("결제 항목이 성공적으로 추가되었습니다.");
        location.href = '/FE/group-complete/Group-before.html?name=' + groupName;

    })
    .catch(error => {
        console.error("결제 항목 생성 오류:", error);
        alert("결제 항목 생성에 실패했습니다.");
    });
}
