document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("member-extra");
    const inputField = document.getElementById("GroupMake-BoxText2");
    const memberList = document.getElementById("member-list");
  
    addButton.addEventListener("click", function () {
      const inputValue = inputField.value.trim();
  
      if (inputValue === "") {
        alert("참가자 이름을 입력해주세요.");
        return;
      }
  
      const existingMembers = Array.from(memberList.children).map(
        (member) => member.textContent.replace(" 삭제", "")
      );
      if (existingMembers.includes(inputValue)) {
        alert("이미 추가된 참가자입니다.");
        return;
      }
  
      const newMember = document.createElement("div");
      newMember.className = "member-item";
      newMember.textContent = inputValue;
  
      const deleteButton = document.createElement("button");
      deleteButton.style.backgroundColor = "transparent"; 
      deleteButton.style.border = "none"; 
      deleteButton.style.cursor = "pointer"; 
  
      const deleteIcon = document.createElement("i");
      deleteIcon.className = "fa-solid fa-xmark";
      deleteIcon.style.color = "#f0f0f0"; 
      deleteIcon.style.fontSize = "15px"; 
      deleteButton.appendChild(deleteIcon);
  
      deleteButton.addEventListener("click", function () {
        if (confirm("정말로 삭제하시겠습니까?")) {
          memberList.removeChild(newMember);
        }
      });
  
      newMember.appendChild(deleteButton);
      memberList.appendChild(newMember);
  
      inputField.value = "";
    });
  });